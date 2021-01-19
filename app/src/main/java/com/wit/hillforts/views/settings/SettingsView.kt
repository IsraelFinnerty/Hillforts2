package com.wit.hillforts.views.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.wit.hillforts.R
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.hillfort.HillfortPresenter
import com.wit.hillforts.views.settings.SettingsPresenter
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.bottom_navigation_settings
import kotlinx.android.synthetic.main.nav_header_main.*

class SettingsView: BaseView() {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        drawerLayout = findViewById(R.id.drawer_layout_settings)
        toolbarSettings.title = title
        toolbarSettings.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbarSettings)

        presenter = initPresenter( SettingsPresenter(this)) as SettingsPresenter

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbarSettings.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }



        nav_view_settings.setNavigationItemSelectedListener { menuItem -> navDrawer(menuItem) }

        btnUpdate.setOnClickListener() { presenter.doClickListener() }

        settingsPassword.setOnClickListener(){   presenter.doSendPasswordReset()   }

        bottom_navigation_settings.setOnNavigationItemSelectedListener { item ->  bottomNavigation(item)  }
   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_up -> finish()
            R.id.item_logout -> basePresenter?.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }



}