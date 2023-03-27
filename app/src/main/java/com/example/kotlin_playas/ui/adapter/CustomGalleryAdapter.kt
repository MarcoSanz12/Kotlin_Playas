package com.example.kotlin_playas.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.data.model.beach.Image
import com.example.kotlin_playas.databinding.ItemBeachBinding
import com.example.kotlin_playas.databinding.ItemGalleryBinding

class CustomGalleryAdapter(private val imageList: List<Image>)
    : RecyclerView.Adapter<GalleryViewHolder>(){

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val persoView = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery,parent,false)
        return GalleryViewHolder(persoView, mListener)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = imageList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}

