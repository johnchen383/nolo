package com.example.nolo.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.nolo.R;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.store.IBranch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.util.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private final String TAG_DIVIDER = "___";
    private final int ANIMATION_INTERVAL = 200;

    private IItemVariant entryVariant;
    private GoogleMap mMap;
    private ViewHolder vh;
    private boolean isModalOpen;

    private class ViewHolder {
        SupportMapFragment mapFragment;
        ImageButton backBtn;
        FrameLayout mapContainer;
        TextView modalHeader;

        public ViewHolder() {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            backBtn = findViewById(R.id.back_btn);
            mapContainer = findViewById(R.id.map_container);
            modalHeader = findViewById(R.id.modal_header);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        vh = new ViewHolder();
        isModalOpen = false;
        entryVariant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        initListeners();
        vh.mapFragment.getMapAsync(this);

    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            finish();
        });

        vh.modalHeader.setOnClickListener(v -> {
            toggleModal();
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

        List<StoreVariant> vars = GetAllItemsUseCase.getAllItems().get(0).getStoreVariants();

        LatLng loc = null;
        for (IStoreVariant v : vars) {
            IStore store = GetStoreByIdUseCase.getStoreById(v.getStoreId());
            for (IBranch branch : store.getBranches()) {
                loc = LocationUtil.getLatLngFromGeoPoint(branch.getGeoPoint());

                Bitmap iconBmp = createMapIcon(getDisplayPrice(v.getBasePrice()));
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBmp))
                        .anchor(0.5f, 1));
                marker.setTag(createMarkerTag(store.getStoreId(), branch.getBranchName()));

            }
        }

        mMap.setOnMarkerClickListener(marker -> onMarkerClick(marker));

        if (entryVariant == null && loc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            return;
        }

        IBranch centredBranch = null;

        for (IBranch branch : GetStoreByIdUseCase.getStoreById(entryVariant.getStoreId()).getBranches()){
            if (branch.getBranchName().equals(entryVariant.getBranchName())){
                centredBranch = branch;
                break;
            }
        }

        if (centredBranch != null){
            LatLng centredLoc = LocationUtil.getLatLngFromGeoPoint(centredBranch.getGeoPoint());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(centredLoc));
        }
    }

    private String getDisplayPrice(double basePrice){
        return String.format("$%.0f", basePrice);
    }

    private Bitmap createMapIcon(String price) {
        final int WIDTH = 160;
        final int HEIGHT = 150;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(WIDTH, HEIGHT, conf);
        Canvas canvas = new Canvas(bmp);

        //icon
        Bitmap mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.marker_img);
        // scale bitmap
        int h = 92; // height in pixels
        int w = 80; // width in pixels
        Bitmap scaledBg = Bitmap.createScaledBitmap(mBackground, w, h, true);
        canvas.drawBitmap(scaledBg, (WIDTH - w) / 2, HEIGHT - h, null);

        //rectangle
        int margin = 5;
        int radius = 10;
        Paint color = new Paint();
        color.setColor(getColor(R.color.white));
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

    private boolean onMarkerClick(final Marker marker) {
        if (!isModalOpen){
            toggleModal();
        }
        // Retrieve the data from the marker.
        String branchName = getBranchNameFromMarkerTag((String) marker.getTag());

        System.out.println(branchName);
        // Check if a click count was set, then display the click count.
        if (branchName != null) {
            Toast.makeText(this, branchName, Toast.LENGTH_LONG).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void toggleModal() {
        float old, target;

        if (isModalOpen){
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
            LinearLayout.LayoutParams newParams = (LinearLayout.LayoutParams) vh.mapContainer.getLayoutParams();
            newParams.weight = val;
            vh.mapContainer.setLayoutParams(newParams);
        });

        anim.setDuration(ANIMATION_INTERVAL);
        anim.start();
    }
}