package com.example.my_watch_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your_api_base_url.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun fetchDataFromServer(): SampleResponse? {
        return sampleResponse()
    }

    private suspend fun sampleResponse(): SampleResponse? {
        return apiService.fetchItpList("https://dummyjson.com/products/1")
    }
}