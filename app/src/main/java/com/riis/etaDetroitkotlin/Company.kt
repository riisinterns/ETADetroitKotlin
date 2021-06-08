package com.riis.etaDetroitkotlin

import androidx.room.Entity
import androidx.room.PrimaryKey

//CREATING THE MODEL LAYER FOR THE APP

//data class creates objects which represent different bus companies and their attributes
@Entity
data class Company(@PrimaryKey val id: Int = 0,
                   var name: String = "",
                   var brandColor: String = "",
                   var busImgUrl: String = "")