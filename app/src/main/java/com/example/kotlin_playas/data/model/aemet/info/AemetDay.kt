package com.example.kotlin_playas.data.model.aemet.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AemetDay {
    @SerializedName("estadoCielo")
    @Expose
    var estadoCielo: AemetStat? = null

    @SerializedName("viento")
    @Expose
    var viento: AemetStat? = null

    @SerializedName("oleaje")
    @Expose
    var oleaje: AemetStat? = null

    @SerializedName("tMaxima")
    @Expose
    private var tMaxima: AemetElement? = null

    @SerializedName("sTermica")
    @Expose
    private var sTermica: AemetElement? = null

    @SerializedName("tAgua")
    @Expose
    private var tAgua: AemetElement? = null

    @SerializedName("uvMax")
    @Expose
    var uvMax: AemetElement? = null

    @SerializedName("fecha")
    @Expose
    var fecha: Int? = null

    @SerializedName("tmaxima")
    @Expose
    var tmaxima: AemetElement? = null

    @SerializedName("stermica")
    @Expose
    var stermica: AemetStat? = null

    @SerializedName("tagua")
    @Expose
    var tagua: AemetElement? = null


}