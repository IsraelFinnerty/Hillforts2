package com.wit.hillforts.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort.toolbarAdd
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity: AppCompatActivity() {

    lateinit var app: MainApp
    var user = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        progressBar.visibility = View.GONE


        btnLogin.setOnClickListener() {
            var currentUser = app.users.findUserByEmail(email.text.toString())
            if (currentUser == null) toast(getString(R.string.email_not_found))
            else {
                user = currentUser
                if (password.text.toString() == user.password)  startActivityForResult( intentFor<HillfortListActivity>().putExtra("User", user), 0)
                else toast(getString(R.string.incorrect_password))
            }
        }

        btnSignup.setOnClickListener() {
            startActivityForResult(intentFor<SignupActivity>(), 0)
        }
    }


}