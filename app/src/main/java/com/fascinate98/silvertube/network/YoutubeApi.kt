package com.fascinate98.silvertube.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YoutubeApi {
    fun apiInstance(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}