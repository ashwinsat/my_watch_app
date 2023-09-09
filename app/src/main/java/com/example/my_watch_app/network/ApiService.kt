package com.example.my_watch_app.network

import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun fetchItpList(@Url url: String): SampleResponse?
}