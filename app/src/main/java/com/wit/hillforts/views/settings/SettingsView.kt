package com.wit.hillforts.views.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.wit.hillforts.R
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.settings.SettingsPresenter
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsView: BaseView() {

    lateinit var presenter: SettingsPresenter
    lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        drawerLayout = findViewById(R.id.drawer_layout_settings)
        toolbarSettings.title = title
        toolbarSettings.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbarSettings)


        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbarSettings.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        presenter = SettingsPresenter(this)

        btnUpdate.setOnClickListener() {
            presenter.doClickListener()
        }

   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_up -> finish()
            R.id.item_logout -> presenter.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }



}