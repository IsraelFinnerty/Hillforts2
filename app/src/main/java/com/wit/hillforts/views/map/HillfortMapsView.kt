package com.wit.hillforts.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import com.wit.hillforts.R

import com.wit.hillforts.helpers.readImageFromPath
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.views.BaseView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_hillfort_maps.toolbar
import kotlinx.android.synthetic.main.card_hillfort.view.*

class HillfortMapsView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: HillfortMapPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        presenter = initPresenter (HillfortMapPresenter(this)) as HillfortMapPresenter

        drawerLayout = findViewById(R.id.drawer_layout_map)
        toolbar.title = title
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbar)


        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbar.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        nav_view_maps.setNavigationItemSelectedListener { menuItem -> navDrawer(menuItem) }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }

        bottom_navigation_map.setOnNavigationItemSelectedListener { item ->  bottomNavigation(item)  }
    }

    override fun showHillfort(hillfort: HillfortModel) {
        currentTitle.text = hillfort.name
        currentDescription.text = hillfort.description
        Glide.with(currentImage).load(hillfort.image1).into(currentImage);
    }

    override fun showHillforts(hillforts: List<HillfortModel>) {
        presenter.doPopulateMap(map, hillforts)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}