package com.example.my_watch_app.models

import android.location.Location
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

class LocationData {
    var startTime: Timestamp? = null
    var endTime: Timestamp? = null
    var name: String? = null
    var type: String? = null
    var severity: String? = null
    var geo: GeoPoint? = null
    var radius: Long? = null
    var location: Location? = null

    override fun equals(other: Any?): Boolean {
        if (other !is LocationData) {
            return false
        }
        return geo == other.geo
    }
}






