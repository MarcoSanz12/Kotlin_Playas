package com.example.kotlin_playas.data.model.aemet.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AemetPrediction {
    @SerializedName("dia")
    @Expose
    var dia: List<AemetDay>? = null
}