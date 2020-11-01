package com.wit.hillforts.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.wit.hillforts.models.HillfortJSONStore
import com.wit.hillforts.models.HillfortStore

class MainApp : Application(), AnkoLogger {
  lateinit var hillforts: HillfortStore
  lateinit var users : HillfortStore

  override fun onCreate() {
    super.onCreate()
    users = HillfortJSONStore(applicationContext)
    hillforts = HillfortJSONStore(applicationContext)
    info("Hillfort App Started")
      }
}