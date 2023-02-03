package com.intercam.folklore.view.activities

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.intercam.folklore.databinding.ActivityDetalleVideoBinding


class DetalleVideo : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, OnMapReadyCallback{

    private lateinit var binding: ActivityDetalleVideoBinding
    private lateinit var map: GoogleMap
    private val TAG = "DetalleVideo"

    //Para los permisos
    private var coarseLocationPermissionGranted = false
    var position = LatLng(0.0,0.0)


    companion object{

        val api_key = ""
        var id_etnia = 0
        var etnia = ""
        var danza = ""
        var desc_danza = ""
        var informacion = ""

        var video_danza = ""
        var vestido = ""
        var latitud: Double = 0.0
        var longitud: Double = 0.0
        var entidad = ""



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)




        with(binding.map) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }


        }


        intent.extras?.let {
            if (it.containsKey("ID")) {
                val id = it.getInt("ID")
                id_etnia = id
            }
            if (it.containsKey("ETNIA")) {
                val et = it.getString("ETNIA")
                etnia = et.toString()
            }
            if (it.containsKey("DANZA")) {
                val et = it.getString("DANZA")
                danza = et.toString()
            }
            if (it.containsKey("DESC_DANZA")) {
                val et = it.getString("DESC_DANZA")
                desc_danza = et.toString()
            }
            if (it.containsKey("VIDEO_DANZA")) {
                val et = it.getString("VIDEO_DANZA")
                video_danza = et.toString()
            }

            if (it.containsKey("LATITUD")) {
                val et = it.getDouble("LATITUD")
                if (et != null) {
                    latitud = et.toDouble()
                }
            }

            if (it.containsKey("LONGITUD")) {
                val et = it.getDouble("LONGITUD")
                if (et != null) {
                    longitud = et.toDouble()
                }
            }

            if (it.containsKey("INFORMACION")) {
                val et = it.getString("INFORMACION")
                informacion = et.toString()
            }
            if (it.containsKey("VESTIDO")) {
                val et = it.getString("VESTIDO")
                vestido = et.toString()
            }
            if (it.containsKey("ENTIDAD")) {
                val et = it.getString("ENTIDAD")
                entidad = et.toString()
            }

        }
        Log.e(TAG, "--->"+latitud + longitud)

        Log.e(TAG, "descDanza"+ desc_danza)

        position = LatLng(latitud,longitud)

        binding.tvTitle.text = etnia
        binding.tvdanza.text = danza
        binding.tvDescdanza.text = desc_danza
        binding.info.text = informacion

        Glide.with(this@DetalleVideo)
            .load(vestido)
            .circleCrop()
            .into(binding.image)

       //poner el mapa

            binding.vvVideo.initialize(api_key, this)

        binding.atras.setOnClickListener{
            Log.i("detalle", "atras")
            val intent = Intent(this@DetalleVideo, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {


            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 6f))
            setMinZoomPreference(6.0f);
            setMaxZoomPreference(12.0f);



            addMarker(MarkerOptions().position(position).title(entidad)
                .snippet(danza))
            mapType = GoogleMap.MAP_TYPE_NORMAL

            val uiSettings: UiSettings = map.uiSettings
            uiSettings.isCompassEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.setAllGesturesEnabled(true)
            uiSettings.isMyLocationButtonEnabled = true

            setOnMapClickListener {
                moveCamera(CameraUpdateFactory.newLatLngZoom(it, 6f))
                Log.d(TAG,"moove"+ it)
                Toast.makeText(this@DetalleVideo, "Clicked on map", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        updateOrRequestPermissions()
    }
    fun createMarker(){
        //19.464089018712155, -99.14044295836308
        val coordinates = LatLng(19.322326, -99.184592)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(entidad)
            .snippet(danza)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_star))


        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }
    override fun onRestart() {
        super.onRestart()
        if(!::map.isInitialized) return
        /*if(!fineLocationPermissionGranted){
            updateOrRequestPermissions()
        }*/
    }



    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (p1 != null) {
            p1.loadVideo(video_danza)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Log.i("error DetalleVideo", p1.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateOrRequestPermissions() {

        //Revisando los permisos
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        coarseLocationPermissionGranted = hasCoarseLocationPermission


        //Solicitando los permisos

        val permissionsToRequest = mutableListOf<String>()

        if (!hasCoarseLocationPermission)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (!hasFineLocationPermission)
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)


        /*if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISO_LOCALIZACION
            )
        } else {

            //Tenemos los permisos
            map.isMyLocationEnabled = true

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                10F,
                this
            )*/
        }





}


