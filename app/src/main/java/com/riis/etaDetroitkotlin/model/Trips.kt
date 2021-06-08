package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["trip_id", "route_id"], unique = true)],
    tableName = "trips"
)
data class Trips(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
    @ColumnInfo(name = "route_id") val routeId: Int,
    @ColumnInfo(name = "direction_id") val directionId: Int
)