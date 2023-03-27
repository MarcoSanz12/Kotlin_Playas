package com.example.kotlin_playas.ui.view.detail

import android.animation.LayoutTransition
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Image
import com.example.kotlin_playas.databinding.FragmentDetailBinding
import com.example.kotlin_playas.ui.adapter.CustomGalleryAdapter
import com.example.kotlin_playas.ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding

    private val viewmodel :DetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailBinding.bind(view)
        viewmodel.isLoading.observe(viewLifecycleOwner){
            if(it){
                binding.pbDFloadingBar.visibility = View.VISIBLE
            }
            else{
                binding.pbDFloadingBar.visibility = View.GONE
            }
        }
        viewmodel.loadAemetInfo()

        binding.rvFdGallery.adapter =
            CustomGalleryAdapter(emptyList(),)

        binding.ivFdFullScreenImage.setOnClickListener{
            it.visibility=View.GONE}

        viewmodel.selBeach.observe(viewLifecycleOwner){
            binding.tvDfBeachname.text = it.title
            Glide.with(view.context).load(it.mainImage?.first()?.url).into(binding.ivDeMainPhoto)

            binding.tvFdDescription.text = it.description?.fromHtml()
            binding.tvFdAccess.text = it.access?.fromHtml()
            binding.tvFdAccessibility.text = it.accessibility?.fromHtml()

            val imageList : List<Image> = it.imageGallery ?: emptyList()
            var adapter = CustomGalleryAdapter(imageList)
            adapter.setOnItemClickListener(object:CustomGalleryAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val image = imageList[position]
                    onImageSelected(image)
                }
            })

            binding.rvFdGallery.adapter = adapter

            if (it.phone != null){
                binding.tvFdPhoneNo.text = it.phone
                dialAction(binding.llFdPhoneNo,it.phone!!)
            }

            if (it.email != null){
                binding.tvFdEmail.text = it.email
                sendEmailAction(binding.llFdEmail,it.email!!)

            }

            if (it.web != null){
                goToLinkAction(binding.ivFdIconWeb,it.web!!)
            }

            if (it.facebook != null){
                goToLinkAction(binding.ivFdIconFacebook,it.facebook!!)
            }

            if (it.twitter != null){
                goToLinkAction(binding.ivFdIconTwitter,it.twitter!!)
            }

            if (it.instagram != null){
                goToLinkAction(binding.ivFdIconInstagram,it.instagram!!)
            }
        }

        viewmodel.aemetInfo.observe(viewLifecycleOwner){

            val symbolUrl = viewmodel.getSymbolUrl(it.prediccion?.dia?.first()?.estadoCielo?.descripcion1!!)
            Glide.with(view.context).load(symbolUrl).into(binding.ivDeTimePhoto)
        }

        opacityOnClick(binding.ivDeTimePhoto)
        prepareExpansionCards()

    }

    private fun prepareExpansionCards() {
        // Expandir descripcion
        expandCard(
            binding.cvFdDescription,
            binding.ivFdDescription,
            binding.tvFdDescription,
            binding.clFdDescription
        )
        // Expandir acceso
        expandCard(
            binding.cvFdAccess,
            binding.ivFdAccess,
            binding.tvFdAccess,
            binding.clFdAccess
        )
        // Expandir acceso
        expandCard(
            binding.cvFdAccessibility,
            binding.ivFdAccessibility,
            binding.tvFdAccessibility,
            binding.clFdAccessibility
        )
        // Expandir contactos
        expandCard(
            binding.cvFdContact,
            binding.ivFdContact,
            binding.clFdContact2,
            binding.clFdContact
        )

        // Expandir galería
        expandCard(
            binding.cvFdGallery,
            binding.ivGallery,
            binding.rvFdGallery,
            binding.clFdGallery
        )
    }

    private fun opacityOnClick (imageview : ImageView){
        imageview.setOnClickListener{
            if (it.alpha == 1F){
                it.alpha = 0.2F
            }else{
                it.alpha = 1F
            }
        }
    }

    private fun onImageSelected(image : Image){
        Log.e("click","galeria clicked")
        var imageView = binding.ivFdFullScreenImage
        imageView.visibility = View.VISIBLE
        Glide.with(imageView.context).load(image.url).into(imageView)

    }
    private fun expandCard (card : CardView, symbol : ImageView, content : View, layout: ViewGroup){
        TransitionManager.beginDelayedTransition(layout,AutoTransition())
        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        card.setOnClickListener{

            //Ocultar
            if (content.visibility == View.VISIBLE) {
                symbol.setImageResource(R.drawable.expand_more)
                content.visibility = View.GONE
            }
            //Expandir
            else{
                symbol.setImageResource(R.drawable.expand_less)
                content.visibility = View.VISIBLE
            }
        }
    }

    fun String.fromHtml():String{
        return Html.fromHtml(this,Html.FROM_HTML_OPTION_USE_CSS_COLORS).toString()
    }

    private fun dialAction(view: View, phoneNo : String){
        view.setOnClickListener{
            var intent : Intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$phoneNo"))
            startActivity(intent)
        }
    }

    private fun goToLinkAction(view: View, link : String){
        view.setOnClickListener{
            var intent : Intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(link))
            startActivity(intent)
        }
    }

    private fun sendEmailAction(view:View, mailCC : String){
        view.setOnClickListener{
            var intent : Intent = Intent(Intent.ACTION_SEND)

            intent.putExtra(Intent.EXTRA_EMAIL,mailCC)

            intent.type = "message/rfc822";

            startActivity(Intent.createChooser(intent,getString(R.string.chooseMail)))

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailFragment()

    }
}