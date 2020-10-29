package com.wit.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.toolbarAdd
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class LoginActivity: AppCompatActivity() {

    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)


        btnLogin.setOnClickListener() {
            startActivityForResult(intentFor<HillfortListActivity>(), 0)
        }

        btnSignup.setOnClickListener() {
            startActivityForResult(intentFor<SignupActivity>(), 0)
        }
    }
}