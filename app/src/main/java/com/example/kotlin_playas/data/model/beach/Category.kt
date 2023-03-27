package com.example.kotlin_playas.data.model.beach

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category {
    @SerializedName("target_id")
    @Expose
    var targetId: Int? = null

    @SerializedName("target_type")
    @Expose
    var targetType: String? = null

    @SerializedName("target_uuid")
    @Expose
    var targetUuid: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null
}