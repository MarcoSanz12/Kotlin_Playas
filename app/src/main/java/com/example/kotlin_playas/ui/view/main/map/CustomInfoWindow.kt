package com.example.kotlin_playas.ui.view.main.map

import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Beach
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class CustomInfoWindow(private val mapView: MapView, private val beach: Beach,
                       private val onClickListener: (Beach) -> Unit) : InfoWindow(R.layout.custom_info_window,mapView) {
    override fun onOpen(item: Any?) {
        closeAllInfoWindowsOn(mapView)

        val tv_title = mView.findViewById<TextView>(R.id.tv_ciwTitle)
        val iv_image = mView.findViewById<ImageView>(R.id.iv_ciwImage)

        iv_image.setOnClickListener {
            onClickListener(beach)
        }

        tv_title.text = beach.title

        Glide.with(mView.context).load(beach.mainImage?.first()?.url).into(iv_image)
    }

    override fun onClose() {
       close()
    }

}