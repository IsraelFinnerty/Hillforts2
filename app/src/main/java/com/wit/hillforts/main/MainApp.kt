package com.wit.hillforts.main

import android.app.Application
import com.wit.hillforts.helpers.JSONCreator
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.wit.hillforts.models.HillfortJSONStore
import com.wit.hillforts.models.HillfortStore

class MainApp : Application(), AnkoLogger {
  lateinit var hillforts: HillfortStore

  override fun onCreate() {
    super.onCreate()
    hillforts = HillfortJSONStore(applicationContext)
    info("Hillfort App Started")
  }
}