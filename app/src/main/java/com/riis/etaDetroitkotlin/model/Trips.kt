package com.riis.etaDetroitkotlin.model

import androidx.room.*

@Entity(
//    indices = [Index(value = ["trip_id", "route_id"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Routes::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("route_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Directions::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("direction_id"),
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "trips",
    indices = [Index(
        name =
        "idx_trips_fk_trips_directions", unique = false, value = ["direction_id"]
    ), Index(
        name =
        "idx_trips_fk_trips_routes", unique = false, value = ["route_id"]
    ), Index(value = ["trip_id", "route_id"], unique = true)]
)
data class Trips(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
    @ColumnInfo(name = "route_id") val routeId: Int,
    @ColumnInfo(name = "direction_id") val directionId: Int
)