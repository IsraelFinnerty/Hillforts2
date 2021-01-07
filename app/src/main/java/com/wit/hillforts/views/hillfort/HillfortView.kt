package com.wit.hillforts.views.hillfort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
    var user = User()
    lateinit var app: MainApp
    val IMAGE_REQUEST1 = 1
    val IMAGE_REQUEST2 = 2
    val IMAGE_REQUEST3 = 3
    val IMAGE_REQUEST4 = 4
    val LOCATION_REQUEST = 5
    lateinit var drawerLayout: DrawerLayout

    lateinit var presenter: HillfortPresenter
    var placemark = HillfortModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        info("Hillfort Activity started..")

        if (intent.hasExtra("User"))
        {
            user = intent.extras?.getParcelable<User>("User")!!
        }

        app = application as MainApp

        drawerLayout = findViewById(R.id.drawer_layout_hillfort)

        init(toolbarAdd)

        toolbarAdd.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        presenter = initPresenter( HillfortPresenter(this)) as HillfortPresenter
        info("Hillfort Activity started..")


        button_visited.setOnCheckedChangeListener { _, isChecked ->
            presenter.doCheckChange(isChecked)
                    }

        btnAdd.setOnClickListener() {
            presenter.doAddOrSave(hillfortName.text.toString(), description.text.toString(), notes.text.toString(), date_visited.year, date_visited.month, date_visited.dayOfMonth   )
                    }

        chooseImage.setOnClickListener { presenter.doSelectImage1() }
        chooseImage2.setOnClickListener { presenter.doSelectImage2() }
        chooseImage3.setOnClickListener { presenter.doSelectImage3() }
        chooseImage4.setOnClickListener { presenter.doSelectImage4() }

        hillfortLocation.setOnClickListener { presenter.doSetLocation() }


        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbarAdd.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        nav_view_hillfort.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_list -> {
                    startActivityForResult(intentFor<HillfortListView>().putExtra("User", user), 0)
                    true
                }
                R.id.nav_settings -> {
                    startActivityForResult(intentFor<SettingsView>().putExtra("User", user), 0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add -> {
                    startActivityForResult(intentFor<HillfortView>().putExtra("User", user),0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    startActivity<LoginView>()
                    true
                }
                else -> {
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }

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
                app.users.delete(hillfort, user)
                startActivityForResult(intentFor<HillfortListView>().putExtra("User", user), 0)
            }
            R.id.item_logout -> startActivity<LoginView>()

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

}

