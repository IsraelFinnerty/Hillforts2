package com.wit.hillforts.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HillfortModel(  var id: Long = 0,
                            var fbId : String = "",
                            var name: String = "",
                            var description: String = "",
                            var image1: String = "",
                            var image2: String = "",
                            var image3: String = "",
                            var image4: String = "",
                            var visited: Boolean = false,
                            var notes: String = "",
                            var dateVisitedYear: Int = 2020,
                            var dateVisitedMonth: Int = 9,
                            var dateVisitedDay: Int = 26,
                            var lat : Double = 0.0,
                            var lng: Double = 0.0,
                            var zoom: Float = 12f) : Parcelable


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
data class User    (var name: String = "",
                    var id: Long = 0,
                    var year: Int = 0,
                    var email: String = "",
                    var password: String = "",
                    var hillforts: MutableList<HillfortModel> = mutableListOf() ): Parcelable