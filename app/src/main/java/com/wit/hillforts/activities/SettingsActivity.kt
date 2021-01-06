package com.wit.hillforts.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.wit.hillforts.R
import com.wit.hillforts.main.MainApp
import com.wit.hillforts.models.User
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SettingsActivity: AppCompatActivity() {

    lateinit var app: MainApp
    var user = User()
    lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        app = application as MainApp
        drawerLayout = findViewById(R.id.drawer_layout_settings)
        toolbarSettings.title = title
        toolbarSettings.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbarSettings)


        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbarSettings.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        nav_view_settings.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_list -> {
                    startActivityForResult(intentFor<HillfortListView>().putExtra("User", user), 0)
                    true
                }
                R.id.nav_settings -> {
                    startActivityForResult(intentFor<SettingsActivity>().putExtra("User", user), 0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add -> {
                    startActivityForResult(intentFor<HillfortView>().putExtra("User", user),0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    startActivity<LoginActivity>()
                    true
                }
                else -> {
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }

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
                startActivityForResult(intentFor<HillfortListView>().putExtra("User", user), 0)
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_up -> finish()
            R.id.item_logout -> startActivity<LoginActivity>()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun isEmailValid(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return EMAIL_REGEX.toRegex().matches(email);
    }
}