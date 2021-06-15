package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["latitude", "longitude"], unique = true)],
    tableName = "stops"
)

data class Stops(
    @PrimaryKey @ColumnInfo(name = "stop_id") val id: Int,
    val name: String = "",
    val latitude: Double,
    val longitude: Double
)