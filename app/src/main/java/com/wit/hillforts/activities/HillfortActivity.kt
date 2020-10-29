package com.wit.hillforts.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import com.wit.hillforts.R
import com.wit.hillforts.helpers.readImage
import com.wit.hillforts.helpers.readImageFromPath
import com.wit.hillforts.helpers.showImagePicker
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.Location
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.HillfortStore
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort.description
import kotlinx.android.synthetic.main.card_hillfort.view.*


class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    var user = User()
    lateinit var app: MainApp
    val IMAGE_REQUEST1 = 1
    val IMAGE_REQUEST2 = 2
    val IMAGE_REQUEST3 = 3
    val IMAGE_REQUEST4 = 4
    val LOCATION_REQUEST = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        info("Hillfort Activity started..")

        if (intent.hasExtra("User"))
        {
            user = intent.extras?.getParcelable<User>("User")!!
        }

        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")

        var edit = false

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortName.setText(hillfort.name)
            description.setText(hillfort.description)
            notes.setText(hillfort.notes)
            button_visited.setChecked(hillfort.visited)
            date_visited.updateDate(hillfort.dateVisitedYear, hillfort.dateVisitedMonth, hillfort.dateVisitedDay)
            btnAdd.setText(R.string.button_editHillfort)

            if (hillfort.visited) {
                date_visited.setVisibility(View.VISIBLE)
                date_title.setVisibility(View.VISIBLE)
            }

            if (hillfort.image1 != "") { chooseImage.setText(R.string.button_changeImage)
                if (hillfort.image1.length > 20) { hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image1))
                } else hillfortImage.setImageResource(this.getResources().getIdentifier(hillfort.image1, "drawable", this.packageName))
            }

            if (hillfort.image2 != "") { chooseImage2.setText(R.string.button_changeImage2)
                if (hillfort.image2.length > 20) { hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.image2))
                } else hillfortImage2.setImageResource(this.getResources().getIdentifier(hillfort.image2, "drawable", this.packageName))
            }

            if (hillfort.image3 != "") { chooseImage3.setText(R.string.button_changeImage3)
                if (hillfort.image3.length > 20) { hillfortImage3.setImageBitmap(readImageFromPath(this, hillfort.image3))
                } else hillfortImage4.setImageResource(this.getResources().getIdentifier(hillfort.image3, "drawable", this.packageName))
            }

            if (hillfort.image4 != "") { chooseImage4.setText(R.string.button_changeImage4)
                if (hillfort.image4.length > 20) { hillfortImage4.setImageBitmap(readImageFromPath(this, hillfort.image4))
                } else hillfortImage4.setImageResource(this.getResources().getIdentifier(hillfort.image4, "drawable", this.packageName))
            }
        }

        hillfortLocation.setOnClickListener {
            val location = Location(51.566804, -9.088011,  10f)
            if (hillfort.lat != 0.0) {
                location.lat =  hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
        }


        button_visited.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                date_visited.setVisibility(View.VISIBLE)
                date_title.setVisibility(View.VISIBLE)
                hillfort.visited = true
            }
            else {
                date_visited.setVisibility(View.GONE)
                date_title.setVisibility(View.GONE)
                hillfort.visited = false
            }
        }

        btnAdd.setOnClickListener() {
            hillfort.name = hillfortName.text.toString()
            hillfort.description = description.text.toString()
            hillfort.notes = notes.text.toString()
            hillfort.dateVisitedYear = date_visited.year
            hillfort.dateVisitedMonth =date_visited.month
            hillfort.dateVisitedDay = date_visited.dayOfMonth
            if (hillfort.name.isNotEmpty()) {
                if (edit) {
                    app.users.update(hillfort.copy(), user)
                } else app.users.create(hillfort.copy(), user)
                info("add Button Pressed: ${hillfort}")
                setResult(AppCompatActivity.RESULT_OK)
                startActivityForResult( intentFor<HillfortListActivity>().putExtra("User", user), 0)
            } else {
                toast(getString(R.string.enter_title))
            }
        }

        chooseImage.setOnClickListener { showImagePicker(this, IMAGE_REQUEST1) }
        chooseImage2.setOnClickListener { showImagePicker(this, IMAGE_REQUEST2) }
        chooseImage3.setOnClickListener { showImagePicker(this, IMAGE_REQUEST3) }
        chooseImage4.setOnClickListener { showImagePicker(this, IMAGE_REQUEST4) }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> finish()
            R.id.item_delete -> {app.users.delete(hillfort, user)
                startActivityForResult( intentFor<HillfortListActivity>().putExtra("User", user), 0)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST1 -> {
                if (data != null) {
                    hillfort.image1 = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.button_changeImage)
                }
            }
            IMAGE_REQUEST2 -> {
                    if (data != null) {
                        hillfort.image2 = data.getData().toString()
                        hillfortImage2.setImageBitmap(readImage(this, resultCode, data))
                        chooseImage2.setText(R.string.button_changeImage2)
                    }
                }
            IMAGE_REQUEST3 -> {
                    if (data != null) {
                        hillfort.image3 = data.getData().toString()
                        hillfortImage3.setImageBitmap(readImage(this, resultCode, data))
                        chooseImage3.setText(R.string.button_changeImage3)
                    }
                }

            IMAGE_REQUEST4 -> {
                    if (data != null) {
                        hillfort.image4 = data.getData().toString()
                        hillfortImage4.setImageBitmap(readImage(this, resultCode, data))
                        chooseImage4.setText(R.string.button_changeImage4)
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

