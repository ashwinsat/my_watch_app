package com.example.my_watch_app.database

import com.example.my_watch_app.models.LocationData
import com.google.firebase.firestore.FirebaseFirestore

class FireBaseDBManager {
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
                    val fieldValue = data["field_name"]

                    // Do something with the data
                    // ...
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the fetch operation
                // For example, you can log the error message
                println("Error fetching documents: $exception")
            }
        return returnValue
    }
}