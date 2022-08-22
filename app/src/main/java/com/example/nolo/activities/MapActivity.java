package com.example.nolo.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.nolo.R;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.store.IBranch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.example.nolo.util.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final String TAG_DIVIDER = "___";
    private ViewHolder vh;

    private class ViewHolder {
        SupportMapFragment mapFragment;
        ImageButton backBtn;

        public ViewHolder(){
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            backBtn = findViewById(R.id.back_btn);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        vh = new ViewHolder();
        initListeners();
        vh.mapFragment.getMapAsync(this);
    }

    private void initListeners(){
        vh.backBtn.setOnClickListener(v -> {
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

        List<StoreVariant> vars = GetAllItemsUseCase.getAllItems().get(0).getStoreVariants();
        LatLng currLoc = null;

        for (IStoreVariant v : vars) {
            IStore store = GetStoreByIdUseCase.getStoreById(v.getStoreId());
            for (IBranch branch : store.getBranches()) {
                currLoc = LocationUtil.getLatLngFromGeoPoint(branch.getGeoPoint());
                Marker marker = mMap.addMarker(new MarkerOptions().position(currLoc).title(branch.getBranchName()));
                marker.setTag(createMarkerTag(store.getStoreId(), branch.getBranchName()));

            }
        }

        // Add a marker in Sydney and move the camera

        if (currLoc != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));

        mMap.setOnMarkerClickListener(marker -> onMarkerClick(marker));
    }

    private String createMarkerTag(String storeId, String branchName){
        return storeId + TAG_DIVIDER + branchName;
    }

    private String getStoreIdFromMarkerTag(String tag){
        return tag.split(TAG_DIVIDER)[0];
    }

    private String getBranchNameFromMarkerTag(String tag){
        return tag.split(TAG_DIVIDER)[1];
    }

    private boolean onMarkerClick(final Marker marker) {

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
}