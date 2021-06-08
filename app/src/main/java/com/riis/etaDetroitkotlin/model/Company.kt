package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//data class creates objects which represent different bus companies and their attributes
@Entity(tableName = "companies")

data class Company(
    @PrimaryKey val id: Int = 0,
    var name: String = "",
    @ColumnInfo(name = "brand_color") var brandColor: String = "",
    @ColumnInfo(name = "bus_image_url") var busImgUrl: String = ""
)


