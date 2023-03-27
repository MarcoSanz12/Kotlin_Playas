package com.example.kotlin_playas.data.model.aemet.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AemetElement {
    @SerializedName("value")
    @Expose
    var value: String? = null

    @SerializedName("valor1")
    @Expose
    var valor1: Int? = null
}