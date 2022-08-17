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

public class LocationPermissions {
    private static FusedLocationProviderClient fusedLocationClient;

    public static boolean hasLocationPermissions(@NonNull android.content.Context context) {
        return !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    public static void getCurrentLocation(android.content.Context context, Consumer<Location> locationCallback) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationCallback.accept(null);
            return;
        }

        fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null)
                .addOnSuccessListener(location -> locationCallback.accept(location));
    }
}
