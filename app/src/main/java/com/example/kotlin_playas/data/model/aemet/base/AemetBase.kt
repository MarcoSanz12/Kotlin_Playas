package com.example.kotlin_playas.data.model.aemet.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AemetBase {

    @SerializedName("datos")
    @Expose
    var data : String? = null

}