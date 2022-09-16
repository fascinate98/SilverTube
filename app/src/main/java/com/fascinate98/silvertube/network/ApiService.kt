package com.fascinate98.silvertube.network

import com.google.api.services.youtube.model.PlaylistItemListResponse
import com.google.api.services.youtube.model.PlaylistListResponse
import org.jetbrains.annotations.TestOnly
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers("accept: application/json",
        "content-type: application/json")
    @GET("playlistItems")
    fun playlistItems(
        @Query("part") partString: String,
        @Query("playlistId") playlistId: String,
        @Query("key") apiKey: String = "AIzaSyAb8eUHQ0J3AR7kXplp0giqr6EJiFKqS3I"
    ) : Call<ListResponse>

    @Headers("accept: application/json",
        "content-type: application/json")
    @GET("playlists")
    fun playList(
        @Query("part") partString: String,
        @Query("channelId") channelId: String,
        @Query("key") apiKey: String = "AIzaSyAb8eUHQ0J3AR7kXplp0giqr6EJiFKqS3I"
    ) : Call<ListResponse>
}