package com.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.wit.hillforts.models.HillfortStore
import com.wit.hillforts.models.firebase.HillfortFireStore

class MainApp : Application(), AnkoLogger {
  lateinit var hillforts: HillfortStore

  override fun onCreate() {
    super.onCreate()

    hillforts = HillfortFireStore(applicationContext)
    info("Hillfort App Started")
      }
}