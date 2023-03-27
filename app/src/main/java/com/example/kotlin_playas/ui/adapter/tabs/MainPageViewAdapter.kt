package com.example.kotlin_playas.ui.adapter.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kotlin_playas.ui.view.main.BeachListFragment
import com.example.kotlin_playas.ui.view.main.MapFragment

class MainPageViewAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> BeachListFragment()
            else -> MapFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }




}