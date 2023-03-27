package com.example.kotlin_playas.ui.view.main.map

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import com.example.kotlin_playas.R
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.bonuspack.clustering.StaticCluster
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class DynamicRadiusMarkerClusterer(val ctx: Context) : RadiusMarkerClusterer(ctx) {

    override fun buildClusterMarker(cluster: StaticCluster, mapView: MapView): Marker {
        val m = Marker(mapView)
        m.position = cluster.position
        m.setInfoWindow(null)
        m.setAnchor(mAnchorU, mAnchorV)

        val mDensityDpi = ctx.resources.displayMetrics.densityDpi

        var finalIcon = Bitmap.createBitmap(
            mClusterIcon.getScaledWidth(mDensityDpi),
            mClusterIcon.getScaledHeight(mDensityDpi) , mClusterIcon.config
        )
        val iconCanvas : Canvas = Canvas(finalIcon)

        val persoPaint = Paint().apply {
            this.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(ctx, getColorBySize(cluster.size)),
                PorterDuff.Mode.SRC_IN)
        }

        iconCanvas.drawBitmap(mClusterIcon,0f,0f,persoPaint)
        var text : String = "${cluster.size}"

        val textHeight = (mTextPaint.descent() + mTextPaint.ascent()).toInt()

        iconCanvas.drawText(
            text,
            mTextAnchorU * finalIcon.width,
            mTextAnchorV * finalIcon.height - textHeight / 2,
            mTextPaint
        )

        val scaleMultiplier = getScaleBySize(cluster.size)
        finalIcon = finalIcon.scale((finalIcon.width * scaleMultiplier).toInt(),(finalIcon.height * scaleMultiplier).toInt(),false)

        m.icon = BitmapDrawable(mapView.context.resources, finalIcon)

        return m
    }

    private fun getScaleBySize (listSize: Int) : Float{

        var multiplier = when(listSize){
            in (0 ..3) -> 1f
            in (4..6) -> 1.05f
            in (7..10) -> 1.15f
            in(10..15)-> 1.30f
            else ->  1.5f
        }

        return (100f + (listSize * multiplier))/100f

    }
    private fun getColorBySize (listSize : Int) : Int {

        return when(listSize){
            in (0 ..3) -> R.color.cluster_red1
            in (4..6) -> R.color.cluster_orange2
            in (7..10) -> R.color.cluster_yellow3
            in(10..15)-> R.color.cluster_green4
            else ->  R.color.cluster_blue5
        }

    }
}