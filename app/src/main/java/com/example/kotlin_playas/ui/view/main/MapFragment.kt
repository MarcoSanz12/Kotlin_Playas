package com.example.kotlin_playas.ui.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.example.kotlin_playas.R
import com.example.kotlin_playas.data.model.beach.Beach
import com.example.kotlin_playas.databinding.FragmentMapBinding
import com.example.kotlin_playas.ui.view.detail.DetailActivity
import com.example.kotlin_playas.ui.view.main.MapFragment.Companion.roundTo
import com.example.kotlin_playas.ui.view.main.map.CustomInfoWindow
import com.example.kotlin_playas.ui.view.main.map.DynamicRadiusMarkerClusterer
import com.example.kotlin_playas.ui.viewmodel.BeachViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import okhttp3.internal.userAgent
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.bonuspack.utils.BonusPackHelper
import org.osmdroid.config.Configuration
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.math.RoundingMode
import java.text.DecimalFormat


const val MARKERS_OVERLAY_0 : Int = 0
const val ROUTE_OVERLAY_1 : Int = 1
const val USER_OVERLAY_2 : Int = 2
const val KML_OVERLAY_3 : Int = 3

const val KML_MURCIA_0 : Int = 0
@AndroidEntryPoint
class MapFragment : Fragment() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map: MapView
    private var cUserPosition: GeoPoint? = null
    private var cTargetPosition : GeoPoint? = null

    private var cIsRoutesVisible : Boolean = true

    private lateinit var binding: FragmentMapBinding

    private val beachViewModel: BeachViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentMapBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        setConfigurePreferences()
        setCopyrightClick()

        binding = FragmentMapBinding.bind(view)

        initMap()

        /**
         * Muestra la barra de carga mientras estén cargando las playas
         */
        beachViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbFmLoadingBar.visibility = View.VISIBLE
            } else {
                binding.pbFmLoadingBar.visibility = View.GONE
            }
        }

        beachViewModel.beachList.observe(viewLifecycleOwner) {
            generateMarkers(it, binding.mpFmMainMap)
        }

        beachViewModel.isMapFollowing.observe(viewLifecycleOwner){

            var myOverlay : MyLocationNewOverlay = map.overlays.getFolderOverlay(USER_OVERLAY_2)?.items?.first() as MyLocationNewOverlay
            if (it){
                myOverlay.enableFollowLocation()


            }else{
                myOverlay.disableFollowLocation()
            }
            binding.tbFmBlockMap.isChecked = it
        }

        beachViewModel.isRoutesVisible.observe(viewLifecycleOwner){
            cIsRoutesVisible = it

            if (it == false){
                disablePath(map)
            }
            else{
                binding.tvFmDistance.visibility = View.VISIBLE
            }

        }

        beachViewModel.isMurciaCensored.observe(viewLifecycleOwner){
            setKmlOverlayEnabled(map, KML_MURCIA_0,it)
        }

        beachViewModel.distanceRoute.observe(viewLifecycleOwner){
            binding.tvFmDistance.text = "${ it.roundTo(2)} km"
        }

        /**
         * Esté método supuestamente se encarga de verificar que el personaje
         * quede centrado en la pantalla, no funciona como debería
         */
        map.addMapListener(DelayedMapListener(object : MapListener{
            override fun onScroll(event: ScrollEvent?): Boolean {
                Log.e("scroll","scrolling")

                val folderOverlay = map.overlays.getFolderOverlay(USER_OVERLAY_2) as FolderOverlay
                if (folderOverlay.items.isNotEmpty()) {
                    val locationOverlay: MyLocationNewOverlay =
                        folderOverlay.items.first() as MyLocationNewOverlay

                    beachViewModel.setIsMapFollowing(map.mapCenter == locationOverlay.myLocation)
                }


                return true
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                return true
            }

        }))
        binding.tbFmBlockMap.setOnCheckedChangeListener{
                _: CompoundButton, checked: Boolean ->
            beachViewModel.setIsMapFollowing(checked)
        }
        binding.tbFmShowCensored.setOnCheckedChangeListener{
                _: CompoundButton, b: Boolean ->
            beachViewModel.setIsMurciaCensored(b)
        }
        binding.tbFmShowRouter.setOnCheckedChangeListener{
                _: CompoundButton, b: Boolean ->
            beachViewModel.setIsRoutesVisible(b)
        }


    }

    /**
     * Configuraciones iniciales para funcionamiento del [map]
     */
    private fun setConfigurePreferences() {
        val ctx = requireContext()
        PreferenceManager.getDefaultSharedPreferences(ctx).edit { clear() }
        Configuration.getInstance().apply {
            load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
            userAgentValue = requireContext().packageName
        }
    }

    /**
     * Inicializa el mapa [map]
     */
    private fun initMap() {
        map = binding.mpFmMainMap

        map.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            setMultiTouchControls(true)

            setDestroyMode(false)
            // Iniciar capas
            for (i in 0..3){
                this.overlays.add(FolderOverlay())
            }
            controller.apply {
                this.setZoom(13.5)
                val startPoint = GeoPoint(37.659365, -0.721581)
                this.setCenter(startPoint)
            }
        }

        generateKmls(map)

    }
    /**
     * Genera los marcadores de las playas según los que haya en [beachViewModel]
     */
    private fun generateMarkers(beachList: List<Beach>, map: MapView) {
        val clusterMarker = DynamicRadiusMarkerClusterer(requireContext())
        val clusterIcon =
            BonusPackHelper.getBitmapFromVectorDrawable(requireContext(), R.drawable.cluster_icon)

        clusterMarker.setIcon(clusterIcon)

        clusterMarker.textPaint.textSize = 50f
        clusterMarker.textPaint.color = Color.WHITE
        clusterMarker.textPaint.style = Paint.Style.FILL_AND_STROKE
        clusterMarker.textPaint.strokeJoin = Paint.Join.ROUND
        clusterMarker.textPaint.strokeCap = Paint.Cap.SQUARE
        clusterMarker.textPaint.strokeWidth = 0f
        clusterMarker.textPaint.setShadowLayer(5f, 6f, 6f, Color.BLACK)


        for (beach in beachList) {
            if (beach.ubicacion != null) {
                val lat: Double = beach.ubicacion!!.lat!!.toDouble()
                val lon: Double = beach.ubicacion!!.lon!!.toDouble()
                val infoWindow = CustomInfoWindow(map, beach) { beach ->
                    onItemSelected(beach)
                }

                val geopoint = GeoPoint(lat, lon)

                val m = Marker(map)
                m.infoWindow = infoWindow
                m.position = geopoint
                m.icon = ContextCompat.getDrawable(requireContext(), R.drawable.marker_icon)
                m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                m.setOnMarkerClickListener(object:Marker.OnMarkerClickListener{

                    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
                        marker?.showInfoWindow()
                        cTargetPosition = marker?.position
                        runBlocking {
                            launch {
                                showPath(cUserPosition, cTargetPosition, map,beachViewModel)
                            }
                        }
                        Log.e("targetPosition", "${cTargetPosition?.latitude} / ${cTargetPosition?.longitude}")

                        return true
                    }

                })
                clusterMarker.add(m)

            }
        }

        map.overlays.getFolderOverlay(MARKERS_OVERLAY_0)?.add(clusterMarker)
        generateUserMarker(map)
        map.invalidate()
    }

    /**
     * Crea el overlay del usuario con su posición actual y llama a [updateUserPosition] para que lo
     * muestre y actualice
     */
    private fun generateUserMarker(map: MapView) {

        val icon = BonusPackHelper.getBitmapFromVectorDrawable(
            requireContext(),
            org.osmdroid.library.R.drawable.person
        )

        val mMyLocationOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map).apply {
                enableMyLocation()
                enableFollowLocation()
                setDirectionIcon(icon)
                setDirectionAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            }
        // Actualizar overlay
        val folderOverlay = map.overlays.getFolderOverlay(USER_OVERLAY_2)?.let {
            it.items.removeFirstOrNull()
            it.items.add(mMyLocationOverlay)
            updateUserPosition()

            map.invalidate()
        }

    }

    private fun generateKmls(map:MapView){

        val kmlDocument = KmlDocument()
        kmlDocument.parseKMLStream(resources.openRawResource(R.raw.murcia),null)

        val murciaOverlay = kmlDocument.mKmlRoot.buildOverlay(map,null,null,kmlDocument)
        murciaOverlay.isEnabled=false
        map.overlays.getFolderOverlay(KML_OVERLAY_3)?.items?.let{
            it.add(murciaOverlay)


        }

        map.invalidate()

    }

    /**
     * Permite habilitar o deshabilitad un [Overlay] con KMS dentro del [KML_OVERLAY_3]
     *
     * @param map
     * @param overlayId Id del overlay en el [FolderOverlay] de kms
     * @param boolean Activar o desactivar overlay
     */
    private fun setKmlOverlayEnabled(map:MapView,overlayId : Int, boolean : Boolean){
        map.overlays.getFolderOverlay(KML_OVERLAY_3)?.items?.let {
            it[overlayId].isEnabled = boolean
        }
    }

    /**
     * Mediante un listener usando el GPS del dispositivo se encarga de actualizar la posición del usuario
     * y generar una ruta a su destino mediante [showPath]
     */
    private fun updateUserPosition() {
        val locationListener = object : LocationListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onLocationChanged(location: Location) {
                cUserPosition = GeoPoint(location.latitude, location.longitude)

                runBlocking {
                        launch {
                            showPath(cUserPosition, cTargetPosition, map,beachViewModel)
                        }
                    }
            }
        }

        val ctx = requireContext()
        if (ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager: LocationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,3f, locationListener
            )
        }
    }

    /**
     * Limpia el [ROUTE_OVERLAY_1] que es el que contiene las rutas y vuelve invisible
     * al TextView con la distancia
     *
     * @param map Mapa a realizarle la operación
     */
    private fun disablePath(map:MapView){
        map.overlays.getFolderOverlay(ROUTE_OVERLAY_1)?.items?.clear()
        binding.tvFmDistance.visibility = View.INVISIBLE
        map.invalidate()
    }

    /**
     * Traza una ruta entre 2 puntos ([p0] y [p1]) y la pinta
     */
    private suspend fun showPath(p0: GeoPoint?, p1: GeoPoint?, map:MapView,vm : BeachViewModel) = withContext(Dispatchers.IO) {

        if (cIsRoutesVisible) {
            if (p0 != null && p1 != null) {
                val rm: RoadManager = OSRMRoadManager(requireContext(), userAgent)

                val road: Road = rm.getRoad(arrayListOf(p0, p1))
                val roadOverlay: Polyline = RoadManager.buildRoadOverlay(road).apply {
                    outlinePaint.apply {
                        color = resources.getColor(R.color.route_fill)
                        strokeJoin = Paint.Join.ROUND
                        style = Paint.Style.STROKE
                        setShadowLayer(0f, 1f, 1f, resources.getColor(R.color.route_stroke))
                        strokeCap = Paint.Cap.ROUND
                        strokeWidth = 15f
                    }

                }


                if (roadOverlay.distance > 5) {

                    vm.setDistance(road.mLength)
                    val folder = map.overlays.getFolderOverlay(ROUTE_OVERLAY_1)?.items
                    folder?.removeFirstOrNull()
                    folder?.add(roadOverlay)
                } else {
                    cTargetPosition = null
                }


                map.invalidate()

            }
        }
    }

    /**
     * Se encarga de dar funcionalidad al textView de copyright
     */
    private fun setCopyrightClick() {
        binding.tvFmCopyright.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.openstreetmap.org/copyright/en")
            startActivity(intent)

        }
    }

    /**
     * Lanza el [DetailActivity] con el marker que se haya clickado
     */
    private fun onItemSelected(selectedBeach: Beach) {
        Toast.makeText(context, selectedBeach.title, Toast.LENGTH_SHORT).show()

        beachViewModel.selectBeach(selectedBeach)
        var intent: Intent = Intent(context, DetailActivity::class.java)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var permissionsToRequest = ArrayList<String>()

        for (i in 0..grantResults.size) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE

            )

        }
    }

    /**
     * Retorna un [FolderOverlay] de una lista de [Overlay], si el elemento en cuestión no es [FolderOverlay]
     * explota
     */
    private fun List<Overlay>.getFolderOverlay(position : Int) : FolderOverlay?{
        var folderOverlay : FolderOverlay? = null
        if (this.size > position){
            folderOverlay = this[position] as FolderOverlay
        }
        return folderOverlay
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        var permissionsToRequest = ArrayList<String>()

        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(perm)
            }

        }

        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MapFragment()

        /**
         * Recoge un [Double] y lo redondea a X decimales
         *
         * @param double Numero a redonder
         * @param decimals Cantidad de decimales a poner
         */
        fun Double.roundTo (decimals : Int) : Double{
            var returnDouble = 0.0
            if (decimals > 0) {
                var string = "#."
                for (i in 1..decimals step 1) {
                    string += "#"
                }
                val df = DecimalFormat(string).apply {
                    roundingMode = RoundingMode.HALF_UP
                }
                returnDouble = df.format(this).toDouble()
            }

            return returnDouble
        }

    }
}