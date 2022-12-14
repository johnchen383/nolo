package com.example.nolo.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.adaptors.MapColourOverlayAdaptor;
import com.example.nolo.adaptors.MapColourOverlayDecoration;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IBranch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.util.Display;
import com.example.nolo.util.LocationUtil;
import com.example.nolo.util.ResponsiveView;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private final String TAG_DIVIDER = "___";
    private final int ANIMATION_INTERVAL = 200;

    private List<Marker> markers = new ArrayList<>();
    private List<IStoreVariant> storeVariants = new ArrayList<>();
    private IItemVariant variant;
    private GoogleMap mMap;
    private ViewHolder vh;
    private boolean isModalOpen;
    private Marker activeMarker;
    private double activeMarkerPrice;

    private class ViewHolder {
        SupportMapFragment mapFragment;
        ImageButton backBtn;
        FrameLayout mapContainer;
        TextView modalHeader;
        TextView address;
        TextView price;
        ImageView img;
        TextView title;
        MaterialButton branchBtn;
        RecyclerView coloursList;

        public ViewHolder() {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            backBtn = findViewById(R.id.back_btn);
            mapContainer = findViewById(R.id.map_container);
            modalHeader = findViewById(R.id.modal_header);
            address = findViewById(R.id.address);
            price = findViewById(R.id.price);
            title = findViewById(R.id.title);
            branchBtn = findViewById(R.id.branch_btn);
            coloursList = findViewById(R.id.colours_list);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        vh = new ViewHolder();
        isModalOpen = false;
        variant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        initListeners();
        vh.mapFragment.getMapAsync(this);
        vh.title.setText(variant.getTitle());

        ResponsiveView.setHeight((int) (Display.getScreenHeight(vh.modalHeader) * 0.1), vh.modalHeader);

        LinearLayoutManager coloursLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.coloursList.setLayoutManager(coloursLayoutManager);
        vh.coloursList.addItemDecoration(new MapColourOverlayDecoration());

        initStyles();
    }

    private void initStyles(){
        double addyMargin = Display.getDynamicHeight(vh.address, Display.dpToPx(-45,  this), Display.dpToPx(-55,  this));
        ResponsiveView.setTopMargin((int) addyMargin, vh.address);
        double addyBottomMargin = Display.getDynamicHeight(vh.address, Display.dpToPx(5,  this), Display.dpToPx(30,  this));
        ResponsiveView.setBottomMargin((int) addyBottomMargin, vh.address);

        double branchTop = Display.getDynamicHeight(vh.branchBtn, Display.dpToPx(-100,  this), Display.dpToPx(0,  this));
        ResponsiveView.setTopMargin((int) branchTop, vh.branchBtn);
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            finish();
        });

        vh.modalHeader.setOnClickListener(v -> {
            toggleModal();
        });

        vh.branchBtn.setOnClickListener(v -> {
            DetailsViewModel.itemVariantFromMap = variant;
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(14.0f);

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }

        List<StoreVariant> vars = GetItemByIdUseCase.getItemById(variant.getItemId()).getStoreVariants();

        for (IStoreVariant v : vars) {
            IStore store = GetStoreByIdUseCase.getStoreById(v.getStoreId());
            storeVariants.add(v);

            for (IBranch branch : store.getBranches()) {
                LatLng loc = LocationUtil.getLatLngFromGeoPoint(branch.getGeoPoint());

                Bitmap iconBmp = createMapIcon(getDisplayPrice(v.getBasePrice()));
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBmp))
                        .anchor(0.5f, 1));
                marker.setTag(createMarkerTag(store.getStoreId(), branch.getBranchName()));
                markers.add(marker);

                if (branch.getBranchName().equals(variant.getBranchName()) && store.getStoreId().equals(variant.getStoreId())) {
                    activeMarker = marker;
                    activeMarkerPrice = variant.getNumericalPrice();
                }
            }
        }

        mMap.setOnMarkerClickListener(marker -> onMarkerClick(marker));

        IBranch centredBranch = null;

        for (IBranch branch : GetStoreByIdUseCase.getStoreById(variant.getStoreId()).getBranches()) {
            if (branch.getBranchName().equals(variant.getBranchName())) {
                centredBranch = branch;
                break;
            }
        }

        if (centredBranch != null) {
            LatLng centredLoc = LocationUtil.getLatLngFromGeoPoint(centredBranch.getGeoPoint());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(centredLoc));
        }

        updateFields();
        updateMarkers();
        updateColour();
    }

    private void updateMarkers() {
        List<Marker> cheapestMarkers = new ArrayList<>();
        double price = Double.MAX_VALUE;

        for (Marker marker : markers) {
            String storeId = getStoreIdFromMarkerTag((String) marker.getTag());

            for (IStoreVariant storeVariant : storeVariants) {
                if (storeVariant.getStoreId().equals(storeId)) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(
                            createMapIcon(getDisplayPrice(storeVariant.getBasePrice()), R.drawable.marker_img, R.color.white))
                    );

                    if (storeVariant.getBasePrice() < price) {
                        price = storeVariant.getBasePrice();
                        cheapestMarkers = new ArrayList<>();
                        cheapestMarkers.add(marker);
                    } else if (storeVariant.getBasePrice() == price) {
                        cheapestMarkers.add(marker);
                    }
                    break;
                }
            }
        }

        boolean isActiveCheap = false;

        for (Marker cheapestMarker : cheapestMarkers) {
            if (cheapestMarker.equals(activeMarker)) {
                isActiveCheap = true;
                continue;
            }

            cheapestMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    createMapIcon(getDisplayPrice(price), R.drawable.marker_img, R.color.green))
            );
        }

        if (isActiveCheap) {
            activeMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    createMapIcon(getDisplayPrice(activeMarkerPrice), R.drawable.marker_img_red, R.color.green))
            );
        } else {
            activeMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    createMapIcon(getDisplayPrice(activeMarkerPrice), R.drawable.marker_img_red, R.color.white))
            );
        }
    }

    private String getDisplayPrice(double basePrice) {
        return String.format("$%.0f", basePrice);
    }

    private Bitmap createMapIcon(String price) {
        return createMapIcon(price, R.drawable.marker_img, R.color.white);
    }

    private Bitmap createMapIcon(String price, int markerImg, int colour) {
        final int WIDTH = 160;
        final int HEIGHT = 150;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(WIDTH, HEIGHT, conf);
        Canvas canvas = new Canvas(bmp);

        //icon
        Bitmap mBackground = BitmapFactory.decodeResource(getResources(), markerImg);
        // scale bitmap
        int h = 92; // height in pixels
        int w = 80; // width in pixels
        Bitmap scaledBg = Bitmap.createScaledBitmap(mBackground, w, h, true);
        canvas.drawBitmap(scaledBg, (WIDTH - w) / 2, HEIGHT - h, null);

        //rectangle
        int margin = 5;
        int radius = 10;
        Paint color = new Paint();
        color.setColor(getColor(colour));
        canvas.drawRoundRect(new RectF(0, 0, WIDTH, HEIGHT - h - margin), radius, radius, color);

        //text
        color = new Paint();
        color.setTextSize(35);
        color.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        color.setColor(getColor(R.color.navy));
        canvas.drawText(price, 30, 40, color);

        return bmp;
    }

    private String createMarkerTag(String storeId, String branchName) {
        return storeId + TAG_DIVIDER + branchName;
    }

    private String getStoreIdFromMarkerTag(String tag) {
        return tag.split(TAG_DIVIDER)[0];
    }

    private String getBranchNameFromMarkerTag(String tag) {
        return tag.split(TAG_DIVIDER)[1];
    }

    private void updateFields() {
        IStore variantStore = GetStoreByIdUseCase.getStoreById(variant.getStoreId());
        vh.modalHeader.setText(variantStore.getStoreName() + " " + variant.getBranchName());

        List<Branch> branches = variantStore.getBranches();

        for (Branch branch : branches) {
            if (branch.getBranchName().equals(variant.getBranchName())) {
                vh.address.setText(branch.getAddress());
                break;
            }
        }

        for (IStoreVariant storeVariant : storeVariants) {
            if (storeVariant.getStoreId().equals(variant.getStoreId())) {
                vh.price.setText(getDisplayPrice(storeVariant.getBasePrice()));
            }
        }
    }

    private void updateColour() {
        for (IStoreVariant storeVariant : storeVariants) {
            if (storeVariant.getStoreId().equals(variant.getStoreId())) {
                vh.coloursList.setAdapter(new MapColourOverlayAdaptor(this, storeVariant.getColours(), variant));
            }
        }
    }

    private boolean onMarkerClick(final Marker marker) {
        if (!isModalOpen) {
            toggleModal();
        }

        // Retrieve the data from the marker.
        String branchName = getBranchNameFromMarkerTag((String) marker.getTag());
        String storeId = getStoreIdFromMarkerTag((String) marker.getTag());

        if (branchName != null && storeId != null) {
            variant.setBranchName(branchName);
            variant.setStoreId(storeId);

            updateColour();

            updateFields();
            activeMarker = marker;
            activeMarkerPrice = variant.getNumericalPrice();
            updateMarkers();
        }

        return false;
    }

    private void toggleModal() {
        float old, target;

        if (isModalOpen) {
            //if open, then close
            old = 0.6f;
            target = 0.9f;
            isModalOpen = false;
        } else {
            //if closed, then open
            old = 0.9f;
            target = 0.6f;
            isModalOpen = true;
        }

        ValueAnimator anim = ValueAnimator.ofFloat(old, target);
        anim.addUpdateListener(valueAnimator -> {
            float val = (Float) valueAnimator.getAnimatedValue();
            ResponsiveView.setWeight(val, vh.mapContainer);
        });

        anim.setDuration(ANIMATION_INTERVAL);
        anim.start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stationery, R.anim.slide_down);
    }
}