package com.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.toolbarAdd
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class SignupActivity: AppCompatActivity() {

    lateinit var app: MainApp
    var user = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)


        btnNewSignup.setOnClickListener() {
            user.name = signup_name.text.toString()
            user.year = signup_year.text.toString().toInt()
            user.email = signup_email.text.toString()
            user.password = signup_password.text.toString()
            if (user.name.isEmpty()) toast(getString(R.string.enter_name))
            else if (user.email.isEmpty()) toast(getString(R.string.enter_email))
            else if (user.password.isEmpty()) toast(getString(R.string.enter_password))
            else if (user.year == 0) toast(getString(R.string.enter_year))
            else {
                app.users.createUser(user.copy())
                startActivityForResult(intentFor<HillfortListActivity>().putExtra("User", user), 0)
            }
        }


    }
}