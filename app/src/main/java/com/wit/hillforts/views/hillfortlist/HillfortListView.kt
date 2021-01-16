package com.wit.hillforts.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import com.wit.hillforts.R
import com.wit.hillforts.activities.HillfortMapsActivity
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.hillfort.HillfortView
import com.wit.hillforts.views.login.LoginView
import com.wit.hillforts.views.settings.SettingsView
import kotlinx.android.synthetic.main.activity_hillfort_list.drawer_layout
import org.jetbrains.anko.startActivity


class HillfortListView : BaseView(), HillfortListener {

    lateinit var app: MainApp
    lateinit var drawerLayout: DrawerLayout
    var user = User()

    lateinit var presenter: HillfortListPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp
        if (intent.hasExtra("User"))
        {
            user = intent.extras?.getParcelable<User>("User")!!
        }


        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar.title = title
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbar)

        presenter = HillfortListPresenter(this)

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbar.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        nav_view.setNavigationItemSelectedListener { menuItem ->
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


        presenter.loadHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_settings -> presenter.doShowSettings()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_fav -> presenter.doFav()
            R.id.item_map -> presenter.doMap()
            }
        return super.onOptionsItemSelected(item)
    }



    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

   override fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

}



