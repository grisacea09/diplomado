package com.intercam.folklore.view.fragments.ui

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.intercam.folklore.R
import com.intercam.folklore.databinding.FragmentMapaBinding


class mapaFragment : Fragment(), OnMapReadyCallback, LocationListener {
   private lateinit var mapaBinding: FragmentMapaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mapaBinding = FragmentMapaBinding.inflate(inflater,container,false)


        return mapaBinding.root

    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }


}