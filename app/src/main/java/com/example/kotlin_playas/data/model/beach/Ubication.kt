package com.example.kotlin_playas.data.model.beach

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Ubication {
    @SerializedName("lat")
    @Expose
    var lat: String? = null

    @SerializedName("lon")
    @Expose
    var lon: String? = null
}