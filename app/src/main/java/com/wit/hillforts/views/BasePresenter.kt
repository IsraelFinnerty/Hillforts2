package com.wit.hillforts.views

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.wit.hillforts.main.MainApp
import kotlinx.android.synthetic.main.nav_header_main.*

open class BasePresenter(var view: BaseView?) {

    var app: MainApp =  view?.application as MainApp

    open fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

    }

    open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

    open fun onDestroy() {
        view = null
    }
}