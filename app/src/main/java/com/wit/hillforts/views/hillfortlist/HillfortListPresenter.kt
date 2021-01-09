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
import org.jetbrains.anko.*

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {


    var user = User()
    var fav = false
    init {


        if (view.intent.hasExtra("User")) {
            user = view.intent.extras?.getParcelable<User>("User")!!
        }

        if (view.intent.hasExtra("Fav")) {
            fav = true
        }
    }

    fun loadHillforts() {
          doAsync {
                val hillforts = app.hillforts.findAll()
                var favHillforts = mutableListOf<HillfortModel>()
                uiThread {
                    if (fav) {
                        for (element in hillforts) {
                            if (element.fav == true) {
                                favHillforts.add(element)
                            }
                        }
                        view?.showHillforts(favHillforts)
                    } else {
                        view?.showHillforts(hillforts)
                    }
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

    fun doFav()
    {
          view?.navigateTo(VIEW.FAVS, 0, "Fav", user)
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

}
