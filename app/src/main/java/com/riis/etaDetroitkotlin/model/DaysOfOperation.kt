package com.riis.etaDetroitkotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days_of_operation")
data class DaysOfOperation(@PrimaryKey val id: Int, val day: String = "")