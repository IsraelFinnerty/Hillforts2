package com.wit.hillforts.views.settings

import android.content.ContentValues
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.wit.hillforts.R
import com.wit.hillforts.views.BasePresenter
import com.wit.hillforts.views.BaseView
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    val hillforts = app.hillforts.findAll()
    var totalHillforts = 0
    var hillfortsVisited = 0
    var favs = 0
    var recentVisited =  LocalDate.of(1900, 10, 31)
    var recentHillfort = ""

    init {
        view.settingsEmail.setText(user!!.email)

        for (hillfort in hillforts) {
            totalHillforts++
            if (hillfort.visited) {
                hillfortsVisited++
                view.statsRecent.setVisibility(View.VISIBLE)
                var currentVisited = LocalDate.of(
                    hillfort.dateVisitedYear,
                    hillfort.dateVisitedMonth + 1,
                    hillfort.dateVisitedDay
                )
                if (currentVisited.isAfter(recentVisited)) {
                    recentVisited = currentVisited
                    recentHillfort = hillfort.name
                }
            }
            if (hillfort.fav) favs++
           }

        view.statsTotal.setText("Total Hillforts: $totalHillforts")
        view.statsVisited.setText("Hillforts Visited: $hillfortsVisited")
        view.statsRecent.setText(
            "Most Recent Visit: $recentHillfort ${
                recentVisited.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                )
            }"
        )
        view.statsFavs.setText("Favourites: $favs")
    }


    fun doClickListener() {
        val updateEmail = view?.updateEmail!!.text.toString()
        val confirmEmail = view?.confirmEmail!!.text.toString()

        if (isEmailValid(updateEmail) == false || isEmailValid(confirmEmail) == false)
        {
            view?.toast(R.string.email_invalid)
        }
        else if (updateEmail != confirmEmail) {
            view?.toast("Email Mismatch")
        }
        else if(updateEmail == null){
            view?.toast("Enter an Email Address")
        }
        else if(confirmEmail == null){
           view?.toast("No Verify Email Address Entered")
        }
        else {
            doUpdateEmail(updateEmail)
            view?.toast("Email Updated")
        }
    }


    fun doUpdateEmail(email: String) {
        user!!.updateEmail(email)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d(ContentValues.TAG, "Email address updated")
                }
            }
    }

    fun doSendPasswordReset(){
        if (user!!.email != null) {
            auth.sendPasswordResetEmail(user!!.email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view?.toast("Password Reset Email Sent")
                    }
                }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}