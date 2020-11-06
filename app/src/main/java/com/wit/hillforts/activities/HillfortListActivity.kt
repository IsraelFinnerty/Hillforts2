package com.wit.hillforts.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort_list.drawer_layout
import kotlinx.android.synthetic.main.activity_nav_drawer.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.startActivity


class HillfortListActivity : AppCompatActivity(), HillfortListener   {

    lateinit var app: MainApp
    lateinit var drawerLayout: DrawerLayout
    var user = User()



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
                    startActivityForResult(intentFor<HillfortListActivity>().putExtra("User", user), 0)
                    true
                }
                R.id.nav_settings -> {
                    startActivityForResult(intentFor<SettingsActivity>().putExtra("User", user), 0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add -> {
                    startActivityForResult(intentFor<HillfortActivity>().putExtra("User", user),0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    startActivity<LoginActivity>()
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


        loadHillforts(user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult(intentFor<HillfortActivity>().putExtra("User", user),0)
            R.id.item_settings -> startActivityForResult(intentFor<SettingsActivity>().putExtra("User", user),0)
            R.id.item_logout -> startActivity<LoginActivity>()
            }
        return super.onOptionsItemSelected(item)
    }



    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("User", user).putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts(user)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHillforts(user: User) {
        val currentUser = app.users.findUserByEmail(user.email)
        if (currentUser != null) showHillfort(currentUser.hillforts)
    }

    fun showHillfort (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}



