package com.wit.hillforts.models

interface HillfortStore {
    fun findAll(user: User): List<User>
    fun create(hillfort: HillfortModel, user: User)
    fun update(hillfort: HillfortModel, user: User)
    fun delete(hillfort: HillfortModel, user: User)
    fun createUser(user: User)
    fun updateUser(user: User)
    fun findUserByEmail(email: String): User?
}