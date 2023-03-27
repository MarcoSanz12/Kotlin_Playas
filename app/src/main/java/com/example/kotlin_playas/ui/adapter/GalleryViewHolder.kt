package com.example.kotlin_playas.ui.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Image

class GalleryViewHolder (view: View, listener: CustomGalleryAdapter.OnItemClickListener) : RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun render(image: Image){

        val iv_image = itemView.findViewById<ImageView>(R.id.iv_imageGallery)

        Glide.with(iv_image.context).load(image.url).into(iv_image)

    }

}
