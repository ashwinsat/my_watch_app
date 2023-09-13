package com.example.my_watch_app.database

import android.location.Location
import android.util.Log
import com.example.my_watch_app.models.LocationData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FireBaseDBManager {

    companion object {
        var hazardPoints = mutableSetOf<LocationData>()
    }

    private var firebaseDB: FirebaseFirestore? = null

    private fun getFireBaseDBManager(): FirebaseFirestore {
        if (firebaseDB == null) {
            firebaseDB = FirebaseFirestore.getInstance()
        }
        return firebaseDB as FirebaseFirestore
    }

    fun getAllHazardLocations(): MutableList<LocationData> {

        val docRef =
            getFireBaseDBManager().collection("Projects").document("26851").collection("Locations")
        val returnValue = mutableListOf<LocationData>()
        docRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot) {
                    // Access the data of each document
                    val data = documentSnapshot.data

                    // Access specific fields
                    val locationData = LocationData()
                    locationData.startTime = data["StartTime"] as Timestamp?
                    locationData.endTime = data["EndTime"] as Timestamp?
                    locationData.name = data["Name"].toString()
                    locationData.type = data["Type"].toString()
                    locationData.geo = data["geo"] as GeoPoint?
                    locationData.radius = data["radius"] as Long?

                    locationData.location = Location("")
                    locationData.location!!.latitude = locationData.geo?.latitude ?: 0.0
                    locationData.location!!.longitude = locationData.geo?.longitude ?: 0.0
                    locationData.location
                    hazardPoints.add(locationData)
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching documents: $exception")
            }
        return returnValue
    }

    fun addHazard(
        location: Location,
        title: String,
        type: String
    ) {
        val currentTimestampMillis = System.currentTimeMillis()
        val currentDate = Date(currentTimestampMillis)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = simpleDateFormat.format(currentDate)

        val data = hashMapOf(
            "StartTime" to formattedDate,
            "radius" to 30,
            "Name" to title,
            "Type" to type,
            "EndTime" to formattedDate, // Added the same for testing
            "geo" to GeoPoint(location.latitude, location.longitude)
        )
        getFireBaseDBManager().
        collection("Projects").
        document("26851").
        collection("Locations")
            .add(data)
            .addOnSuccessListener { documentReference ->
                // Document added with ID
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Handle errors here
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun getHazardList(): MutableSet<LocationData> {
        return hazardPoints
    }
}