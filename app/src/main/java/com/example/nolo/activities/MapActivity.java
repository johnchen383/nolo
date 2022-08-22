package com.example.nolo.activities;

import android.os.Bundle;

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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getView().setBackgroundColor(getColor(R.color.white));
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<StoreVariant> vars = GetAllItemsUseCase.getAllItems().get(0).getStoreVariants();
        LatLng currLoc = null;

        for (IStoreVariant v : vars){
            IStore store = GetStoreByIdUseCase.getStoreById(v.getStoreId());
            for (IBranch branch : store.getBranches()){
                currLoc = LocationUtil.getLatLngFromGeoPoint(branch.getGeoPoint());
                mMap.addMarker(new MarkerOptions().position(currLoc).title("Marker"));
            }
        }

        // Add a marker in Sydney and move the camera

        if (currLoc != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
    }
}