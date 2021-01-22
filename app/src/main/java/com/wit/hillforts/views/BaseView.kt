package com.wit.hillforts.views

import android.content.ClipData
import android.content.Intent
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.wit.hillforts.R
import com.wit.hillforts.views.map.HillfortMapsView
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.views.hillfort.HillfortView
import com.wit.hillforts.views.hillfortlist.HillfortListView
import com.wit.hillforts.views.login.LoginView
import com.wit.hillforts.views.map.MapView
import com.wit.hillforts.views.settings.SettingsView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, HILLFORT, MAPS, LIST, SETTINGS, FAVS, LOGIN, MAP
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null
    lateinit var drawerLayout: DrawerLayout



    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, HillfortListView::class.java)
        when (view) {
         //   VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.HILLFORT -> intent = Intent(this, HillfortView::class.java)
            VIEW.MAPS -> intent = Intent(this, MapView::class.java)
            VIEW.LIST -> intent = Intent(this, HillfortListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVS -> intent = Intent(this, HillfortListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.MAP -> intent = Intent(this, HillfortMapsView::class.java )
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun bottomNavigation(item: MenuItem): Boolean {
               when(item.itemId) {
                R.id.list_bottom-> {
                    navigateTo(VIEW.LIST)
                    return true
                }
                R.id.fav_bottom-> {
                    navigateTo(VIEW.FAVS, 0, "Fav")
                    return true
                }
                R.id.settings_bottom-> {
                    navigateTo(VIEW.SETTINGS)
                    return true
                }
                R.id.map_bottom-> {
                    navigateTo(VIEW.MAP)
                    return true
                }
                R.id.logout_bottom-> {
                    basePresenter!!.doLogout()
                    return true
                   }
                else -> return false
            }
    }

    fun navDrawer(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_list -> {
                navigateTo(VIEW.LIST)
                return true
            }
            R.id.nav_fav-> {
                navigateTo(VIEW.FAVS, 0, "Fav")
                return true
            }
            R.id.nav_settings -> {
                navigateTo(VIEW.SETTINGS)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_add -> {
                navigateTo(VIEW.HILLFORT)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_map -> {
                navigateTo(VIEW.MAP)
                return true
            }
            R.id.nav_logout -> {
                basePresenter!!.doLogout()
                return true
            }
            else -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
                return false
            }
        }
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showHillfort(hillfort: HillfortModel) {}
    open fun showHillforts(hillforts: List<HillfortModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
    open fun findById(id: Long): HillfortModel? {return HillfortModel()}
}