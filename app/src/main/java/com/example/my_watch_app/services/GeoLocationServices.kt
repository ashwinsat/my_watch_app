package com.example.my_watch_app.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.my_watch_app.database.FireBaseDBManager
import com.example.my_watch_app.models.LocationData
import com.example.my_watch_app.notifications.NotificationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


@Suppress("DEPRECATION")
class GeoLocationServices : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest? = LocationRequest()
    var lastKnownLocation: Location? = null

    companion

    object {
        const val CHANNEL_ID = "LocationServiceChannel"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create a foreground notification to keep the service running
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Hazard service active")
            .setContentText("On the out look for Hazards")
            .setSmallIcon(androidx.wear.tiles.material.R.drawable.avatar)
            .build()

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        startForeground(1, notification)
        return START_STICKY
    }

    private fun createLocationRequest() {
        if (locationRequest == null) {
            locationRequest = LocationRequest()
        }
        locationRequest!!.interval = 10000 // Update interval in milliseconds
        locationRequest!!.fastestInterval = 5000 // Fastest update interval
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.lastLocation?.let { location ->
                    Log.d("GeoLocationServices","Current location : ${location.latitude} | ${location.longitude}")
                    lastKnownLocation = location
                    val hazardLoc = FireBaseDBManager.hazardPoints.find { locData ->
                        val storedLoc = locData.location
                        val radius: Long = locData.radius ?: 0
                        val distance = (storedLoc?.distanceTo(location) ?: 0).toLong()
                        distance < radius
                    }
                    if (hazardLoc != null) {
                        showNotification(hazardLoc)
                    }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle gracefully for production
            return
        }
        locationRequest?.let {
            fusedLocationClient.requestLocationUpdates(
                it,
                locationCallback,
                null
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun showNotification(hazardLoc: LocationData) {
        NotificationHelper().showNotification(
            applicationContext,
            hazardLoc.name ?: "",
            hazardLoc.type ?: ""
        )
    }
}