package com.example.kotlin_playas.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Beach

class CustomBeachListAdapter(private val beachList : List<Beach>, private val onClickListener: (Beach) -> Unit) :
    RecyclerView.Adapter<BeachViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeachViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BeachViewHolder(layoutInflater.inflate(R.layout.item_beach,parent,false))
    }

    override fun onBindViewHolder(holder: BeachViewHolder, position: Int) {
        val item = beachList[position]
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int {
        return beachList.size
    }


}