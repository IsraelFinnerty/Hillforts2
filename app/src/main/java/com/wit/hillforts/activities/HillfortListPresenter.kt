package com.wit.hillforts.activities

import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class HillfortListPresenter(val view: HillfortListView) {

    var app: MainApp
    var user = User()

    init {
        app = view.application as MainApp

        if (view.intent.hasExtra("User"))
        {
            user = view.intent.extras?.getParcelable<User>("User")!!
        }
    }

    fun getHillforts(user: User) {
        val currentUser = app.users.findUserByEmail(user.email)
        if (currentUser != null) doShowHillfort(currentUser.hillforts)
    }

    fun doShowHillfort (hillforts: List<HillfortModel>) {
        view.recyclerView.adapter = HillfortAdapter(hillforts, view)
        view.recyclerView.adapter?.notifyDataSetChanged()
    }


    fun doAddHillfort() {
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("User", user),0)
        }


    fun doEditHillfort(hillfort: HillfortModel) {
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("User", user).putExtra("hillfort_edit", hillfort), 0)
    }

    fun doShowSettings() {
        view.startActivityForResult(view.intentFor<SettingsActivity>().putExtra("User", user),0)
    }

    fun doLogout() {
        view.startActivity<LoginActivity>()
    }

}
