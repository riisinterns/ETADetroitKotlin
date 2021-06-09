package com.riis.etaDetroitkotlin.model

import androidx.room.*

@Entity(
    tableName = "route_stops",
    foreignKeys = [ForeignKey(
        entity = Routes::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("route_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Stops::class,
            parentColumns = arrayOf("stop_id"),
            childColumns = arrayOf("stop_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Directions::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("direction_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ), ForeignKey(
            entity = DaysOfOperation::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("day_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(
        name = "idx_route_stops_fk_route_stops_direction_id",
        value = ["direction_id"],
        unique = false
    ),
        Index(name = "idx_route_stops_fk_route_stops_day_id", value = ["day_id"], unique = false),
        Index(name = "idx_route_stops_fk_route_stops_stop_id", value = ["stop_id"], unique = false)]
)
data class RouteStops(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "route_id") val routeId: Int,
    @ColumnInfo(name = "stop_id") val stopId: Int,
    @ColumnInfo(name = "direction_id") val directionId: Int,
    @ColumnInfo(name = "day_id") val dayId: Int
)
