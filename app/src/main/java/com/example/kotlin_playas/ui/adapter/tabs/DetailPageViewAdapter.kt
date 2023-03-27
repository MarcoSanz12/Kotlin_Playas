package com.example.kotlin_playas.ui.adapter.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kotlin_playas.ui.view.detail.DetailFragment
import com.example.kotlin_playas.ui.view.detail.TimeFragment

class DetailPageViewAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DetailFragment()
            else -> TimeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}