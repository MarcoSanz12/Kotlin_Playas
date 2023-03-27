package com.example.kotlin_playas.data.model.beach

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "beach_table")
class Beach : java.io.Serializable{
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("nid")
    @Expose
    var nid: Int? = null

    @SerializedName("langcode")
    @Expose
    var langcode: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("uid")
    @Expose
    var uid: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("created")
    @Expose
    var created: List<Int>? = null

    @SerializedName("changed")
    @Expose
    var changed: List<Int>? = null

    @SerializedName("content_translation_source")
    @Expose
    var contentTranslationSource: String? = null

    @SerializedName("access")
    @Expose
    var access: String? = null

    @SerializedName("accessibility")
    @Expose
    var accessibility: String? = null

    @SerializedName("category")
    @Expose
    var category: List<Category>? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("facebook")
    @Expose
    var facebook: String? = null

    @SerializedName("files")
    @Expose
    var files: List<File>? = null

    @SerializedName("id_112")
    @Expose
    var id112: Int? = null

    @SerializedName("id_aemet")
    @Expose
    var idAemet: Int? = null

    @SerializedName("image_gallery")
    @Expose
    var imageGallery: List<Image>? = null

    @SerializedName("instagram")
    @Expose
    var instagram: String? = null

    @SerializedName("main_image")
    @Expose
    var mainImage: List<Image>? = null

    @SerializedName("mosaic_image")
    @Expose
    var mosaicImage: List<Image>? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("security")
    @Expose
    var security: String? = null

    @SerializedName("services_and_activities")
    @Expose
    var servicesAndActivities: String? = null

    @SerializedName("summary")
    @Expose
    var summary: String? = null

    @SerializedName("twitter")
    @Expose
    var twitter: String? = null

    @SerializedName("ubicacion")
    @Expose
    var ubicacion: Ubication? = null

    @SerializedName("web")
    @Expose
    var web: String? = null
}