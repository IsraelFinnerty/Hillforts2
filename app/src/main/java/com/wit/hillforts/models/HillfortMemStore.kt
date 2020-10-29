package com.wit.hillforts.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    var users = mutableListOf<User>()

    override fun findAll(user: User): List<User> {
        return users
    }

    override fun create(hillfort: HillfortModel, user: User) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    override fun update(hillfort: HillfortModel, user: User) {
        var foundHillfort: HillfortModel? = hillforts.find { h -> h.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.image1 = hillfort.image1
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            logAll()
        }
    }

    override fun findUserByEmail(email: String): User? {
        return null
    }
    fun logAll() {
        hillforts.forEach { info("${it}") }
    }

    override fun delete(hillfort: HillfortModel, user: User) {
        hillforts.remove(hillfort)
    }

    override fun createUser(user: User) {
        users.add(user)

    }
}