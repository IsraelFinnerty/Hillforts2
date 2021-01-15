package com.wit.hillforts.views.hillfort

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.wit.hillforts.R
import com.wit.hillforts.views.map.MapView
import com.wit.hillforts.helpers.readImage
import com.wit.hillforts.helpers.showImagePicker
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.Location
import com.wit.hillforts.models.User
import com.wit.hillforts.models.firebase.HillfortFireStore
import com.wit.hillforts.views.BasePresenter
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.VIEW
import com.wit.hillforts.views.hillfortlist.HillfortListView
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*


class HillfortPresenter(view: BaseView) : BasePresenter(view) {
    var hillfort = HillfortModel()
    var user = User()

    val IMAGE_REQUEST1 = 1
    val IMAGE_REQUEST2 = 2
    val IMAGE_REQUEST3 = 3
    val IMAGE_REQUEST4 = 4
    val LOCATION_REQUEST = 5
    lateinit var drawerLayout: DrawerLayout
    var edit = false
    var fireStore: HillfortFireStore? = app.hillforts as HillfortFireStore


    init {
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        } // else {
           // if (checkLocationPermissions(view)) {
            //    doSetCurrentLocation()
           // }
       // }
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
            /*
            view?.info("add Button Pressed: ${hillfort}")

            view?.setResult(AppCompatActivity.RESULT_OK)
            */
            view!!.intent.removeExtra("Fav")
            fireStore!!.fetchHillforts {
                //   view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
            }

        } else {
            view?.toast(R.string.enter_title)
        }

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
            val location = Location(51.566804, -9.088011, 10f)
            if (hillfort.lat != 0.0) {
                location.lat = hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            view?.startActivityForResult(
                view?.intentFor<MapView>()!!.putExtra("location", location), LOCATION_REQUEST
            )
        }

        fun doDelete() {
            doAsync {
                app.hillforts.delete(hillfort)
                uiThread {
                    view?.finish()
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
                    if (data != null) {
                        val location = data.extras?.getParcelable<Location>("location")!!
                        hillfort.lat = location.lat
                        hillfort.lng = location.lng
                        hillfort.zoom = location.zoom
                    }
                }
            }
        }

}


