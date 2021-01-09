package com.wit.hillforts.views.login

import android.os.Bundle
import com.wit.hillforts.R
import com.wit.hillforts.views.BaseView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.*



class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      //  toolbarAdd.title = title
       // setSupportActionBar(toolbarAdd)

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                presenter.doSignUp(email,password)
            }
        }

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            }
            else {
                presenter.doLogin(email,password)
            }
        }
    }
}