package com.wit.hillforts.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import com.wit.hillforts.helpers.*
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.models.HillfortStore
import java.util.*

val JSON_FILE = "hillforts.json"
val JSON_LIST = "hillfortlist.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class JSONCreator : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (!exists(context, JSON_FILE)) {
            deserialize()
            serialize()
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        create()
    }


    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { h -> h.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.image1 = hillfort.image1
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            serialize()
        }
    }

    private fun create() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }
    private fun deserialize() {
        val jsonString = read(context, JSON_LIST)
        hillforts = Gson().fromJson(jsonString, listType)
    }

    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
        serialize()

    }
}