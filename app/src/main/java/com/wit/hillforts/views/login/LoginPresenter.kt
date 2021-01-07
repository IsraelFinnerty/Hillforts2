package com.wit.hillforts.views.login

import com.wit.hillforts.views.signup.SignupView
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import org.jetbrains.anko.intentFor

class LoginPresenter(val view: LoginView) {
    var hillfort = HillfortModel()
    var user = User()
    lateinit var app: MainApp

    init {
        app = view.application as MainApp
    }


    fun doSignupBtn()
    {
        view.startActivityForResult(view.intentFor<SignupView>(), 0)
    }
}