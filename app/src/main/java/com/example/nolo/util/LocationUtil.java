package com.example.nolo.util;

import static com.google.android.gms.location.Priority.PRIORITY_LOW_POWER;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.function.Consumer;

public class LocationUtil {
    public static final long TIME_IN_MILLISECONDS_FIVE_MINUTES = 1000*60*5;
    private static FusedLocationProviderClient fusedLocationClient;
    private static android.location.Location cachedLocation = null;
    private static long lastLoadedTime = 0;
    private static android.content.Context ctx;

    public static boolean hasLocationPermissions(@NonNull android.content.Context context) {
        boolean noFineLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean noCoarseLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        return !(noFineLocation && noCoarseLocation);
    }

    public static void loadCurrentLocation(@NonNull android.content.Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        ctx = context;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null)
                .addOnSuccessListener(location -> cachedLocation = location);
    }

    public static android.location.Location getCurrentLocation(){
        reloadCurrentLocationIfExpired();

        return cachedLocation;
    }

    public static float getDistanceBetweenLocationAndGeoPoint(GeoPoint gp){
        android.location.Location loc = getCurrentLocation();

        if (loc == null) return -1;

        android.location.Location gpLoc = new android.location.Location("");
        gpLoc.setLatitude(gp.getLatitude());
        gpLoc.setLongitude(gp.getLongitude());

        return loc.distanceTo(gpLoc);
    }

    private static void reloadCurrentLocationIfExpired() {
        if (System.currentTimeMillis() - lastLoadedTime > TIME_IN_MILLISECONDS_FIVE_MINUTES)
            loadCurrentLocation(ctx);
    }

    public static LatLng getLatLngFromGeoPoint(GeoPoint gp){
        return new LatLng(gp.getLatitude(), gp.getLongitude());
    }
}
