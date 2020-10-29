package com.wit.hillforts.models

interface HillfortStore {
    fun findAll(user: User): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun createUser(user: User)
    fun findUserByEmail(email: String): User?
}