package com.example.kotlin_playas.data.model.aemet.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AemetStat {

    @SerializedName("value")
    @Expose
    var value: String? = null

    @SerializedName("f1")
    @Expose
    var f1: Int? = null

    @SerializedName("descripcion1")
    @Expose
    var descripcion1: String? = null

    @SerializedName("f2")
    @Expose
    var f2: Int? = null

    @SerializedName("descripcion2")
    @Expose
    var descripcion2: String? = null
}