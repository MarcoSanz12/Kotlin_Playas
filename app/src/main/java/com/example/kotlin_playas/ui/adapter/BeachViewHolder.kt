package com.example.kotlin_playas.ui.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.databinding.ItemBeachBinding
import com.bumptech.glide.Glide

class BeachViewHolder(view: View) : ViewHolder(view) {

    var binding = ItemBeachBinding.bind(view)
    // ALTERNATIVE_IMAGE == bicho pelao
    private val ALTERNATIVE_IMAGE = "https://pbs.twimg.com/profile_images/1601159631908339712/ZR4My9iA_400x400.jpg"

    fun render(beachModel : Beach, onClickListener:(Beach) -> Unit){

        binding.tvTitle.text = beachModel.title

        var url = beachModel.mainImage?.get(0)?.url ?: ALTERNATIVE_IMAGE

        Glide.with(binding.imageView.context).load(url).into(binding.imageView)

        itemView.setOnClickListener{
            onClickListener(beachModel)
            Toast.makeText(itemView.context,"CLICKED", Toast.LENGTH_SHORT).show()}

    }
}