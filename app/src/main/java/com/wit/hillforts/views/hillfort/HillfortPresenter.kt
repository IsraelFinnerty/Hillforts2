package com.wit.hillforts.views.hillfort

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.wit.hillforts.R
import com.wit.hillforts.views.map.MapView
import com.wit.hillforts.helpers.readImage
import com.wit.hillforts.helpers.showImagePicker
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.Location
import com.wit.hillforts.models.User
import com.wit.hillforts.views.hillfortlist.HillfortListView
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class HillfortPresenter(val view: HillfortView) {
    var hillfort = HillfortModel()
    var user = User()
    lateinit var app: MainApp
    val IMAGE_REQUEST1 = 1
    val IMAGE_REQUEST2 = 2
    val IMAGE_REQUEST3 = 3
    val IMAGE_REQUEST4 = 4
    val LOCATION_REQUEST = 5
    lateinit var drawerLayout: DrawerLayout
    var edit = false


    init {
        app = view.application as MainApp
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        }

        if (view.intent.hasExtra("User"))
        {
            user = view.intent.extras?.getParcelable<User>("User")!!
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
            if (edit) {
                app.users.update(hillfort.copy(), user)
            } else app.users.create(hillfort.copy(), user)
            view.info("add Button Pressed: ${hillfort}")
            view.setResult(AppCompatActivity.RESULT_OK)
            view.startActivityForResult(
                view.intentFor<HillfortListView>().putExtra("User", user), 0
            )
        } else {
            view.toast(view.getString(R.string.enter_title))
        }

    }

    fun doSelectImage1() {
        showImagePicker(view, IMAGE_REQUEST1)
    }

    fun doSelectImage2() {
        showImagePicker(view, IMAGE_REQUEST2)
    }

    fun doSelectImage3() {
        showImagePicker(view, IMAGE_REQUEST3)
    }

    fun doSelectImage4() {
        showImagePicker(view, IMAGE_REQUEST4)
    }

    fun doSetLocation() {
        val location = Location(51.566804, -9.088011,  10f)
        if (hillfort.lat != 0.0) {
            location.lat =  hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        view.startActivityForResult(view.intentFor<MapView>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doCheckChange(isChecked: Boolean) {
        if (isChecked) {
            view.date_visited.setVisibility(View.VISIBLE)

            view.date_title.setVisibility(View.VISIBLE)
            hillfort.visited = true
        }
        else {
            view.date_visited.setVisibility(View.GONE)
            view.date_title.setVisibility(View.GONE)
            hillfort.visited = false
        }
    }


    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST1 -> {
                if (data != null) {
                    hillfort.image1 = data.getData().toString()
                    view.hillfortImage.setImageBitmap(readImage(view, resultCode, data))
                    view.chooseImage.setText(R.string.button_changeImage)
                }
            }
            IMAGE_REQUEST2 -> {
                if (data != null) {
                    hillfort.image2 = data.getData().toString()
                    view.hillfortImage2.setImageBitmap(readImage(view, resultCode, data))
                    view.chooseImage2.setText(R.string.button_changeImage2)
                }
            }
            IMAGE_REQUEST3 -> {
                if (data != null) {
                    hillfort.image3 = data.getData().toString()
                    view.hillfortImage3.setImageBitmap(readImage(view, resultCode, data))
                    view.chooseImage3.setText(R.string.button_changeImage3)
                }
            }

            IMAGE_REQUEST4 -> {
                if (data != null) {
                    hillfort.image4 = data.getData().toString()
                    view.hillfortImage4.setImageBitmap(readImage(view, resultCode, data))
                    view.chooseImage4.setText(R.string.button_changeImage4)
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

