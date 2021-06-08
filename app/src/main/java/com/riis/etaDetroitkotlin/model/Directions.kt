package com.riis.etaDetroitkotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "directions")
data class Directions(@PrimaryKey val id: Int, val name: String)