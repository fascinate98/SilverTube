package com.fascinate98.silvertube.network


import com.google.gson.annotations.SerializedName

data class ListResponse(

    @SerializedName("items")
    val items: List<Item>,
){
    data class Item(
        @SerializedName("etag")
        val etag: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("kind")
        val kind: String,

        @SerializedName("contentDetails")
        val contentDetails: contentDetails,

        @SerializedName("snippet")
        val snippet: snippet,
    )

    data class contentDetails(
        @SerializedName("videoId")
        val videoId: String
   )
    data class snippet(
        @SerializedName("title")
        val title: String
    )
}
