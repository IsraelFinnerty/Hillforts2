package com.wit.hillforts.views.hillfort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import com.wit.hillforts.R
import com.wit.hillforts.views.login.LoginView
import com.wit.hillforts.views.settings.SettingsView
import com.wit.hillforts.helpers.readImageFromPath
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.hillfortlist.HillfortListView
import kotlinx.android.synthetic.main.activity_hillfort.description
import kotlinx.android.synthetic.main.activity_hillfort.hillfortImage
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*

class HillfortView :  BaseView(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var presenter: HillfortPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        info("Hillfort Activity started..")

        drawerLayout = findViewById(R.id.drawer_layout_hillfort)

        init(toolbarAdd)

        toolbarAdd.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        presenter = initPresenter( HillfortPresenter(this)) as HillfortPresenter
        info("Hillfort Activity started..")

        button_visited.setOnCheckedChangeListener { _, isChecked ->
            presenter.doCheckVisited(isChecked)
                    }

        button_fav.setOnCheckedChangeListener { _, isChecked ->
            presenter.doCheckFav(isChecked)
        }

        ratingBar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(ratignBar: RatingBar?, rating: Float, fromUser: Boolean) {
                presenter.doCheckRating(rating)
            } })

        btnAdd.setOnClickListener() {
            presenter.doAddOrSave(hillfortName.text.toString(), description.text.toString(), notes.text.toString(), date_visited.year, date_visited.month, date_visited.dayOfMonth   )
                    }

        chooseImage.setOnClickListener { presenter.doSelectImage1() }
        chooseImage2.setOnClickListener { presenter.doSelectImage2() }
        chooseImage3.setOnClickListener { presenter.doSelectImage3() }
        chooseImage4.setOnClickListener { presenter.doSelectImage4() }

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbarAdd.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }


        nav_view_hillfort.setNavigationItemSelectedListener { menuItem -> navDrawer(menuItem) }

        bottom_navigation1.setOnNavigationItemSelectedListener { item ->  bottomNavigation(item)  }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (intent.hasExtra("hillfort_edit")) {
            menuInflater.inflate(R.menu.menu_hillfort, menu)
        }
        else menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> finish()
            R.id.item_delete -> {
                presenter.doDelete()
                          }
            R.id.item_logout -> startActivity<LoginView>()
            R.id.item_save ->  presenter.doAddOrSave(hillfortName.text.toString(), description.text.toString(), notes.text.toString(), date_visited.year, date_visited.month, date_visited.dayOfMonth   )

        }
        return super.onOptionsItemSelected(item)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun showHillfort(hillfort: HillfortModel){
        if (hillfortName.text.isEmpty()) hillfortName.setText(hillfort.name)
        if (description.text.isEmpty()) description.setText(hillfort.description)
        if (notes.text.isEmpty())notes.setText(hillfort.notes)
        button_fav.setChecked(hillfort.fav)
        button_visited.setChecked(hillfort.visited)
        date_visited.updateDate(hillfort.dateVisitedYear, hillfort.dateVisitedMonth, hillfort.dateVisitedDay)
        btnAdd.setText(R.string.button_editHillfort)
        ratingBar.setRating(hillfort.rating)

        if (hillfort.visited) {
            date_visited.setVisibility(View.VISIBLE)
            date_title.setVisibility(View.VISIBLE)
        }

        if (hillfort.image1 != "") { chooseImage.setText(R.string.button_changeImage)
            if (hillfort.image1.length > 20) { Glide.with(this).load(hillfort.image1).into(hillfortImage)
            } else hillfortImage.setImageResource(this.getResources().getIdentifier(hillfort.image1, "drawable", this.packageName))
        }

        if (hillfort.image2 != "") { chooseImage2.setText(R.string.button_changeImage2)
            if (hillfort.image2.length > 20) { Glide.with(this).load(hillfort.image2).into(hillfortImage2)
            } else hillfortImage2.setImageResource(this.getResources().getIdentifier(hillfort.image2, "drawable", this.packageName))
        }

        if (hillfort.image3 != "") { chooseImage3.setText(R.string.button_changeImage3)
            if (hillfort.image3.length > 20) {Glide.with(this).load(hillfort.image3).into(hillfortImage3)
            } else hillfortImage4.setImageResource(this.getResources().getIdentifier(hillfort.image3, "drawable", this.packageName))
        }

        if (hillfort.image4 != "") { chooseImage4.setText(R.string.button_changeImage4)
            if (hillfort.image4.length > 20) { Glide.with(this).load(hillfort.image4).into(hillfortImage4)
            } else hillfortImage4.setImageResource(this.getResources().getIdentifier(hillfort.image4, "drawable", this.packageName))
        }
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
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}


