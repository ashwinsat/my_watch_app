package com.example.my_watch_app.geofenceHelper

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.my_watch_app.presentation.MainActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

@Suppress("DEPRECATION")
class GeofenceManager {

    companion object {
        private var instance: GeofenceManager? = null
        private var googleApiClient: GoogleApiClient? = null

        fun getInstance(context: Context): GeofenceManager {
            if (instance == null) {
                instance = GeofenceManager()
                initialize(context = context)
            }
            return instance as GeofenceManager
        }

        private fun initialize(context: Context) {
            // Initialize GoogleApiClient
            googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API).build()
            googleApiClient?.connect()
        }
    }


    fun addGeoFence(
        latitude: Double,
        longitude: Double,
        radiusInMeters: Float,
        context: Context
    ) {

        // Create Geofence Object
        val geofence = Geofence.Builder()
            .setRequestId("your_geofence_id")
            .setCircularRegion(latitude, longitude, radiusInMeters)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        //  Create Geofencing Request
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        // Return if no permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleApiClient?.let {
            LocationServices.GeofencingApi.addGeofences(
                it,
                geofencingRequest,
                getPendingIntent(context)
            ).setResultCallback { status ->
                if (status.isSuccess) {
                    Log.d("GeofenceManager", "Geofences registered successfully")
                } else {
                    Log.d("GeofenceManager", "Geofences registration failed")
                }
            }
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java) // Create new activity for this
        intent.putExtra("key", "value")
        val requestCode = 123 // You can use any unique integer here
        val flags =
            PendingIntent.FLAG_UPDATE_CURRENT // Update the existing PendingIntent if it already exists
        return PendingIntent.getActivity(context, requestCode, intent, flags)
    }
}