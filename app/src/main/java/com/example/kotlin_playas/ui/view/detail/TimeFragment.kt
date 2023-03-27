package com.example.kotlin_playas.ui.view.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.aemet.info.AemetDay
import com.example.kotlin_playas.databinding.FragmentTimeBinding
import com.example.kotlin_playas.ui.viewmodel.DetailViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TimeFragment : Fragment() {

    private lateinit var binding : FragmentTimeBinding

    private val viewmodel : DetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTimeBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTimeBinding.bind(view)

        viewmodel.selBeach.observe(viewLifecycleOwner){
            binding.tvFtBeachname.text = it.title
        }

        viewmodel.isAemetInfo.observe(viewLifecycleOwner){
            if(it){
                binding.tvFtErrorMessage.visibility = View.GONE
            }else{
                binding.tvFtErrorMessage.visibility = View.VISIBLE
            }
        }

        viewmodel.isLoading.observe(viewLifecycleOwner){
            if(it){
                binding.pbFTloadingBar.visibility = View.VISIBLE
            }
            else{
                binding.pbFTloadingBar.visibility = View.GONE
            }
        }

        viewmodel.aemetInfo.observe(viewLifecycleOwner){
            it.prediccion?.dia?.let {
                bindViews(it, view)
            }
        }
    }

    private fun bindViews(it: List<AemetDay>,view: View) {
        val dates = arrayOf<TextView>(binding.tvFtSky0, binding.tvFtSky1, binding.tvFtSky2)
        val skyImages =
            arrayOf<ShapeableImageView>(binding.ivFtSky0, binding.ivFtSky1, binding.ivFtSky2)
        val winds = arrayOf<TextView>(binding.tvFtWind0, binding.tvFtWind1, binding.tvFtWind2)
        val waves = arrayOf<TextView>(binding.tvFtWave0, binding.tvFtWave1, binding.tvFtWave2)
        val temps = arrayOf<TextView>(binding.tvFtTemp0, binding.tvFtTemp1, binding.tvFtTemp2)
        val waterTemps =
            arrayOf<TextView>(binding.tvFtWater0, binding.tvFtWater1, binding.tvFtWater2)
        val uvs = arrayOf<TextView>(binding.tvFtUv0, binding.tvFtUv1, binding.tvFtUv2)
        val noData: String = getString(R.string.noData)
        val tooltipManager = ToolTipsManager()

        for (i in 0..2) {
            val day = it.get(i)

            // Images
            var descripcion = day.estadoCielo?.descripcion1
            var url = viewmodel.getSymbolUrl(descripcion)
            Glide.with(view.context).load(url).into(skyImages[i])

            skyImages[i].setOnClickListener(object : OnClickListener{
                override fun onClick(p0: View?) {
                    val builder = ToolTip.Builder(
                        skyImages[i].context,
                        p0!!,
                        binding.root,
                        // Capitalizar
                        "${descripcion?.capitalized()}",
                        ToolTip.POSITION_BELOW
                    )
                    builder.setAlign(ToolTip.ALIGN_LEFT)
                    tooltipManager.show(builder.build())
                }
            })

            // Dates
            var date: String =
                "${day.fecha.toString().substring(6, 8)} / ${day.fecha.toString().substring(4, 6)}"
            dates[i].text = date

            // Winds
            var wind = day.viento?.descripcion1 ?: noData
            winds[i].text = wind.capitalized()

            // Waves
            var wave = day.oleaje?.descripcion1 ?: noData
            waves[i].text = wave.capitalized()

            // Temps
            var temp = day.tmaxima?.valor1 ?: noData
            var sens = day.stermica?.descripcion1?.capitalized()
            temps[i].text = "${temp}º ($sens)"

            // Water temps
            var waterTemp = day.tagua?.valor1 ?: noData
            waterTemps[i].text = "${waterTemp}º"

            // UVs
            var uv = day.uvMax?.valor1 ?: noData
            uvs[i].text = "$uv"

        }
    }

    fun String.capitalized () : String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TimeFragment()
    }
}