package com.wit.hillforts.views.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.wit.hillforts.R
import com.wit.hillforts.models.Location
import com.wit.hillforts.views.map.MapView

class MapPresenter(var view: MapView) {

    private lateinit var map: GoogleMap
    var location = Location()

    init {
        location = view.intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = view.supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(view)
    }
}