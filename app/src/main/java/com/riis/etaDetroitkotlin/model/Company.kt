package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//CREATING THE MODEL LAYER FOR THE APP

//data class creates objects which represent different bus companies and their attributes
@Entity(tableName = "companies")

data class Company(
    @PrimaryKey val id: Int = 0,
    val name: String = "",
    @ColumnInfo(name = "brand_color") val brandColor: String = "",
    @ColumnInfo(name = "bus_image_url") val busImgUrl: String = ""
)


