package com.wit.hillforts.views.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wit.hillforts.R
import kotlinx.android.synthetic.main.activity_hillfort.toolbarAdd
import kotlinx.android.synthetic.main.activity_signup.*


class SignupView: AppCompatActivity() {


    lateinit var presenter: SignupPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        presenter = SignupPresenter(this)


        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)


        btnNewSignup.setOnClickListener() {
           presenter.doNewSignup()
        }
    }



}