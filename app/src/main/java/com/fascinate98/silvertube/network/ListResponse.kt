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
        val contentDetails: contentDetails
    )

    data class contentDetails(
        val videoId: String
    )
}
