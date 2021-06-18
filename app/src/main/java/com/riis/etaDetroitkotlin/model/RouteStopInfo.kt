package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo

data class RouteStopInfo(
    @ColumnInfo(name = "route_id") val routeId: Int,
    @ColumnInfo(name = "stop_id") val stopId: Int,
    @ColumnInfo(name = "direction_id") val directionId: Int,
    @ColumnInfo(name = "day_id") val dayId: Int,
    val day: String = "",
    val name: String = "",
    val latitude: Double,
    val longitude: Double
)
