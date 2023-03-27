package com.example.kotlin_playas.data.network

import androidx.room.TypeConverter
import com.example.kotlin_playas.data.model.beach.Category
import com.example.kotlin_playas.data.model.beach.File
import com.example.kotlin_playas.data.model.beach.Image
import com.example.kotlin_playas.data.model.beach.Ubication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class BeachConverter {

    @TypeConverter
    fun fromCategoryListToString(categoryList:List<Category>?):String?{
        var string : String = ""
        if (categoryList != null){
            var gson = Gson()
            var type : Type = object : TypeToken<List<Category>>(){}.type

            string = gson.toJson(categoryList,type)
        }

        return string
    }

    @TypeConverter
    fun fromStringToCategoryList (string : String?) : List<Category>?{
        var categoryList : List<Category>? = emptyList()

        if (!string.isNullOrEmpty()){
            var gson = Gson()
            var type : Type = object : TypeToken<List<Category>>(){}.type

            categoryList = gson.fromJson(string,type)
        }

        return categoryList!!
    }

    @TypeConverter
    fun fromFileListToString(fileList:List<File>?):String?{
        var string : String = ""
        if (fileList != null){
            var gson = Gson()
            var type : Type = object : TypeToken<List<File>>(){}.type

            string = gson.toJson(fileList,type)
        }

        return string
    }

    @TypeConverter
    fun fromStringToFileList (string : String?) : List<File>?{
        var fileList : List<File>? = emptyList()

        if (!string.isNullOrEmpty()){
            var gson = Gson()
            var type : Type = object : TypeToken<List<File>>(){}.type

            fileList = gson.fromJson(string,type)
        }

        return fileList!!
    }

    @TypeConverter
    fun fromImageListToString(imageList:List<Image>?):String?{
        var string : String = ""
        if (imageList != null){
            var gson = Gson()
            var type : Type = object : TypeToken<List<Image>>(){}.type

            string = gson.toJson(imageList,type)
        }

        return string
    }

    @TypeConverter
    fun fromStringToImageList (string : String?) : List<Image>?{
        var ImageList : List<Image>? = emptyList()

        if (!string.isNullOrEmpty()){
            var gson = Gson()
            var type : Type = object : TypeToken<List<Image>>(){}.type

            ImageList = gson.fromJson(string,type)
        }

        return ImageList!!
    }

    @TypeConverter
    fun fromIntListToString(intList:List<Int>?):String?{
        var string : String = ""
        if (intList != null){
            var gson = Gson()
            var type : Type = object : TypeToken<List<Int>>(){}.type

            string = gson.toJson(intList,type)
        }

        return string
    }

    @TypeConverter
    fun fromStringToIntList (string : String?) : List<Int>?{
        var intList : List<Int>? = emptyList()

        if (!string.isNullOrEmpty()){
            var gson = Gson()
            var type : Type = object : TypeToken<List<Int>>(){}.type

            intList = gson.fromJson(string,type)
        }

        return intList!!
    }

    @TypeConverter
    fun fromUbicationToString(ubication:Ubication?):String?{
        var string : String = ""
        if (ubication != null){
            var gson = Gson()
            var type : Type = object : TypeToken<Ubication>(){}.type

            string = gson.toJson(ubication,type)
        }

        return string
    }

    @TypeConverter
    fun fromStringToUbication (string : String?) : Ubication?{
        var ubication = Ubication()

        if (!string.isNullOrEmpty()){
            var gson = Gson()
            var type : Type = object : TypeToken<Ubication>(){}.type

            ubication = gson.fromJson(string,type)
        }

        return ubication
    }
}