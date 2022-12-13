package com.sna.stepsgo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var firstName : String = "",
    var lastName : String = "",
    var countsteps: String = ""

){
    @PrimaryKey(autoGenerate = true)  var id: Int = 0
}