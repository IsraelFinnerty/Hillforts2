package com.wit.hillforts.views.settings

import android.view.View
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.User
import com.wit.hillforts.views.hillfortlist.HillfortListView
import com.wit.hillforts.views.login.LoginView
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SettingsPresenter(var view: SettingsView) {
    lateinit var app: MainApp
    var user = User()
   // lateinit var navDrawer: NavDrawer

    var totalHillforts = 0
    var hillfortsVisited = 0
    var notesAdded = 0
    var imagesAdded = 0
    var recentVisited =  LocalDate.of(1900, 10, 31)
    var recentHillfort = ""

    init {
        app = view.application as MainApp

        if (view.intent.hasExtra("User"))
        {
            val currentUser = view.intent.extras?.getParcelable<User>("User")!!
            // user = app.statususers.findUserByEmail(currentUser.email)!!
        }

       // navDrawer = NavDrawer()
       // navDrawer.navigationListener(user)

        view.updateName.setText(user.name)
        view.updateEmail.setText(user.email)
        view.updatePassword.setText(user.password)
        view.updateYear.setText(user.year.toString())

        for (hillfort in user.hillforts) {
            totalHillforts++
            if (hillfort.visited) {
                hillfortsVisited++
                view.statsRecent.setVisibility(View.VISIBLE)
                var currentVisited = LocalDate.of(hillfort.dateVisitedYear, hillfort.dateVisitedMonth+1, hillfort.dateVisitedDay)
                if (currentVisited.isAfter(recentVisited)) {
                    recentVisited=currentVisited
                    recentHillfort = hillfort.name
                }
            }
            if (hillfort.notes != "") notesAdded++
            if (hillfort.image1.length > 20) imagesAdded++
            if (hillfort.image2.length > 20) imagesAdded++
            if (hillfort.image3.length > 20) imagesAdded++
            if (hillfort.image4.length > 20) imagesAdded++

        }

        view.statsTotal.setText("Total Hillforts: $totalHillforts")
        view.statsVisited.setText("Hillforts Visited: $hillfortsVisited")
        view.statsRecent.setText("Most Recent Visit: $recentHillfort ${recentVisited.format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}")
        view.statsNotes.setText("Notes Added: $notesAdded")
        view.statsImages.setText("Images Addded: $imagesAdded")
    }

    fun doClickListener() {
      //  var emailUsed = app.users.findUserByEmail(view.updateEmail.text.toString())
        var previousEmail = user.email
        user.name = view.updateName.text.toString()
        user.year = view.updateYear.text.toString().toInt()
        user.email = view.updateEmail.text.toString()
        user.password = view.updatePassword.text.toString()
        if (user.name.isEmpty()) view.toast(view.getString(R.string.enter_name))
        else if (user.email.isEmpty()) view.toast(view.getString(R.string.enter_email))
        else if (user.password.isEmpty()) view.toast(view.getString(R.string.enter_password))
        else if (user.password.length < 8) view.toast(view.getString(R.string.short_password))
        else if (user.year == 0 || user.year > 4) view.toast(view.getString(R.string.enter_year))
      //  else if (emailUsed != null && emailUsed.email != previousEmail) view.toast(view.getString(R.string.email_used)) // checks for email use on other accounts
        else if (isEmailValid(user.email) == false) view.toast(view.getString(R.string.email_invalid))
        else {
           // app.users.updateUser(user.copy())
            view.startActivityForResult(view.intentFor<HillfortListView>().putExtra("User", user), 0)
        }
    }

    fun doLogout()
    {
        view.startActivity<LoginView>()
    }


    private fun isEmailValid(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}