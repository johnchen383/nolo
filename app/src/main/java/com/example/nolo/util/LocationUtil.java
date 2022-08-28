package com.example.nolo.util;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class LocationUtil {
    /**
     * Check whether the app has location permission
     *
     * @param context
     * @return True if the app has location permission;
     *         Otherwise False
     */
    public static boolean hasLocationPermissions(@NonNull android.content.Context context) {
        boolean noFineLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean noCoarseLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        return !(noFineLocation && noCoarseLocation);
    }

    /**
     * Get Latitude and Longitude from geo point
     *
     * @param gp Geo point
     * @return Latitude and Longitude
     */
    public static LatLng getLatLngFromGeoPoint(GeoPoint gp) {
        return new LatLng(gp.getLatitude(), gp.getLongitude());
    }
}
