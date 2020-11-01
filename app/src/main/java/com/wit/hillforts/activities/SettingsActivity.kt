package com.wit.hillforts.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.toolbarAdd
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SettingsActivity: AppCompatActivity() {

    lateinit var app: MainApp
    var user = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        app = application as MainApp
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        if (intent.hasExtra("User"))
        {
            val currentUser = intent.extras?.getParcelable<User>("User")!!
            user = app.users.findUserByEmail(currentUser.email)!!
        }



        updateName.setText(user.name)
        updateEmail.setText(user.email)
        updatePassword.setText(user.password)
        updateYear.setText(user.year.toString())

        var totalHillforts = 0
        var hillfortsVisited = 0
        var notesAdded = 0
        var imagesAdded = 0
        var recentVisited =  LocalDate.of(1900, 10, 31)
        var recentHillfort = ""
        for (hillfort in user.hillforts) {
            totalHillforts++
            if (hillfort.visited) {
                hillfortsVisited++
                statsRecent.setVisibility(View.VISIBLE)
                var currentVisited = LocalDate.of(hillfort.dateVisitedYear, hillfort.dateVisitedMonth, hillfort.dateVisitedDay)
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

        statsTotal.setText("Total Hillforts: $totalHillforts")
        statsVisited.setText("Hillforts Visited: $hillfortsVisited")
        statsRecent.setText("Most Recent Visit: $recentHillfort ${recentVisited.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))}")
        statsNotes.setText("Notes Added: $notesAdded")
        statsImages.setText("Images Addded: $imagesAdded")



        btnUpdate.setOnClickListener() {
            var emailUsed = app.users.findUserByEmail(updateEmail.text.toString())
            var previousEmail = user.email
            user.name = updateName.text.toString()
            user.year = updateYear.text.toString().toInt()
            user.email = updateEmail.text.toString()
            user.password = updatePassword.text.toString()
            if (user.name.isEmpty()) toast(getString(R.string.enter_name))
            else if (user.email.isEmpty()) toast(getString(R.string.enter_email))
            else if (user.password.isEmpty()) toast(getString(R.string.enter_password))
            else if (user.password.length < 8) toast(getString(R.string.short_password))
            else if (user.year == 0) toast(getString(R.string.enter_year))
            else if (emailUsed != null && emailUsed.email != previousEmail) toast(getString(R.string.email_used)) // checks for email use on other accounts
            else if (isEmailValid(user.email) == false) toast(getString(R.string.email_invalid))
            else {
                app.users.updateUser(user.copy())
                startActivityForResult(intentFor<HillfortListActivity>().putExtra("User", user), 0)
            }
        }
    }


    private fun isEmailValid(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}