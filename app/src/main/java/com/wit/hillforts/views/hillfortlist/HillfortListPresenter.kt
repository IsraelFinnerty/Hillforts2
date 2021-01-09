package com.wit.hillforts.views.hillfortlist

import com.google.firebase.auth.FirebaseAuth
import com.wit.hillforts.views.hillfort.HillfortView
import com.wit.hillforts.views.login.LoginView
import com.wit.hillforts.views.settings.SettingsView
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import com.wit.hillforts.views.BasePresenter
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.VIEW
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {


    var user = User()

    init {


        if (view.intent.hasExtra("User"))
        {
            user = view.intent.extras?.getParcelable<User>("User")!!
        }
    }

    fun loadHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT, 0, "User", user)
    // startActivityForResult(view?.intentFor<HillfortView>().putExtra("User", user),0)
        }


    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
        //startActivityForResult(view?.intentFor<HillfortView>().putExtra("User", user).putExtra("hillfort_edit", hillfort), 0)
    }

    fun doShowSettings() {
        view?.navigateTo(VIEW.SETTINGS, 0, "User", user)
        //startActivityForResult(view?.intentFor<SettingsView>().putExtra("User", user),0)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

}
