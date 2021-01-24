package com.wit.hillforts.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wit.hillforts.R
import com.wit.hillforts.helpers.*
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.Location
import com.wit.hillforts.models.firebase.HillfortFireStore
import com.wit.hillforts.views.BasePresenter
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.VIEW
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*


class HillfortPresenter(view: BaseView) : BasePresenter(view) {
    var hillfort = HillfortModel()
    val IMAGE_REQUEST1 = 1
    val IMAGE_REQUEST2 = 2
    val IMAGE_REQUEST3 = 3
    val IMAGE_REQUEST4 = 4
    val LOCATION_REQUEST = 5
    var edit = false
    var locationManualyChanged = false
    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var fireStore: HillfortFireStore? = null


    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }

        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    fun doAddOrSave(
        name: String,
        description: String,
        notes: String,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        hillfort.name = name
        hillfort.description = description
        hillfort.notes = notes
        hillfort.dateVisitedYear = year
        hillfort.dateVisitedMonth = month
        hillfort.dateVisitedDay = dayOfMonth
        if (hillfort.name.isNotEmpty()) {
            doAsync {
                if (edit) {
                    app.hillforts.update(hillfort)
                } else {
                    app.hillforts.create(hillfort)
                }
                uiThread {
                    view!!.intent.removeExtra("Fav")
                    fireStore!!.fetchHillforts {
                        view?.navigateTo(VIEW.LIST)
                    }

                }
            }
        } else {
            view?.toast(R.string.enter_title)
        }

    }


    fun cacheHillfort(
        hillfortTitle: String,
        description: String,
        notes: String,
        dayOfMonth: Int,
        month: Int,
        year: Int,
        visited: Boolean,
        favourite: Boolean,
        rating: Float
    )
    {
        hillfort.name = hillfortTitle
        hillfort.description = description
        hillfort.notes = notes
        hillfort.dateVisitedDay = dayOfMonth
        hillfort.dateVisitedMonth = month
        hillfort.dateVisitedYear = year
        hillfort.visited

    }

    fun doSelectImage1() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST1)
        }
    }

    fun doSelectImage2() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST2)
        }
    }

    fun doSelectImage3() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST3)
        }
    }

    fun doSelectImage4() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST4)
        }
    }

    fun doSetLocation() {
        locationManualyChanged = true;
        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            Location(hillfort.lat, hillfort.lng, hillfort.zoom)
        )
    }

    fun doDelete() {
        doAsync {
            app.hillforts.delete(hillfort)
            uiThread {
                view?.navigateTo(VIEW.LIST)
            }
        }
    }

    fun doCheckVisited(isChecked: Boolean) {
        if (isChecked) {
            view!!.date_visited.setVisibility(View.VISIBLE)

            view!!.date_title.setVisibility(View.VISIBLE)
            hillfort.visited = true
        } else {
            view!!.date_visited.setVisibility(View.GONE)
            view!!.date_title.setVisibility(View.GONE)
            hillfort.visited = false
        }
    }

    fun doCheckFav(isChecked: Boolean) {
        hillfort.fav = isChecked
    }

    fun doCheckRating(rating: Float) {
        hillfort.rating = rating
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST1 -> {
                if (data != null) {
                    hillfort.image1 = data.data.toString()
                    view?.showHillfort(hillfort)
                }
            }
            IMAGE_REQUEST2 -> {
                if (data != null) {
                    hillfort.image2 = data.data.toString()
                    view?.showHillfort(hillfort)
                }
            }

            IMAGE_REQUEST3 -> {
                if (data != null) {
                    hillfort.image3 = data.data.toString()
                    view?.showHillfort(hillfort)
                }
            }

            IMAGE_REQUEST4 -> {
                if (data != null) {
                    hillfort.image4 = data.data.toString()
                    view?.showHillfort(hillfort)
                }
            }

            LOCATION_REQUEST -> {

                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    locationUpdate(hillfort.lat, hillfort.lng)

            }
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(hillfort.lat, hillfort.lng)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.lat, hillfort.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        hillfort.lat = lat
        hillfort.lng = lng
        hillfort.zoom = 8f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options =
            MarkerOptions().title(hillfort.name).position(LatLng(hillfort.lat, hillfort.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(hillfort.lat, hillfort.lng),
                hillfort.zoom
            )
        )
        view?.showHillfort(hillfort)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if (!locationManualyChanged) {
                        locationUpdate(l.latitude, l.longitude)
                    }
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}


