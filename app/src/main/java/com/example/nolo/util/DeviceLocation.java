package com.example.nolo.util;

import static com.google.android.gms.location.Priority.PRIORITY_LOW_POWER;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.function.Consumer;

public class DeviceLocation {
    public static final long TIME_IN_MILLISECONDS_FIVE_MINUTES = 1000*60*5;
    private static FusedLocationProviderClient fusedLocationClient;
    private static Location cachedLocation = null;
    private static long lastLoadedTime = 0;
    private static android.content.Context ctx;

    public static boolean hasLocationPermissions(@NonNull android.content.Context context) {
        return !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
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

    public static Location getCurrentLocation(){
        reloadCurrentLocationIfExpired();

        return cachedLocation;
    }

    private static void reloadCurrentLocationIfExpired() {
        if (System.currentTimeMillis() - lastLoadedTime > TIME_IN_MILLISECONDS_FIVE_MINUTES)
            loadCurrentLocation(ctx);
    }
}