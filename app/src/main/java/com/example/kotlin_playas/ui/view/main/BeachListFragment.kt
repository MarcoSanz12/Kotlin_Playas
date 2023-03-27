package com.example.kotlin_playas.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.databinding.FragmentBeachListBinding
import com.example.kotlin_playas.ui.adapter.CustomBeachListAdapter
import com.example.kotlin_playas.ui.view.detail.DetailActivity
import com.example.kotlin_playas.ui.viewmodel.BeachViewModel


class BeachListFragment : Fragment() {

    private lateinit var binding : FragmentBeachListBinding

    private val beachViewModel : BeachViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBeachListBinding.inflate(layoutInflater)

        beachViewModel.onCreate(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beach_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBeachListBinding.bind(view)

        initList()
    }

    private fun initList(){

        val decoration = DividerItemDecoration(context,LinearLayoutManager(context).orientation)

        binding.rvRecyclerView.addItemDecoration(decoration)

        binding.rvRecyclerView.adapter =
            CustomBeachListAdapter(emptyList()){
            beach ->
                onItemSelected(beach)
            }

        beachViewModel.beachList.observe(viewLifecycleOwner) {
            binding.tvListCount.text = "Beach List [${it.size}]"
            binding.rvRecyclerView.adapter =
                CustomBeachListAdapter(it){
                        beach ->
                    onItemSelected(beach)
                }

        }

        beachViewModel.isLoading.observe(viewLifecycleOwner){
                isLoading ->
            binding.pbLoading.isVisible = isLoading
        }

    }


    private fun onItemSelected(selectedBeach: Beach){
        Toast.makeText(context,selectedBeach.title, Toast.LENGTH_SHORT).show()

            beachViewModel.selectBeach(selectedBeach)
            var intent:Intent = Intent(context, DetailActivity::class.java)
            startActivity(intent)

    }

    companion object {
        @JvmStatic
        fun newInstance() = BeachListFragment()
    }
}