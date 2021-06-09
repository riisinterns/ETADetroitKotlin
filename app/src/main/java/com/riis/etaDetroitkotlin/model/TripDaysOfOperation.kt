package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "trip_days_of_operation",
    foreignKeys = [ForeignKey(
        entity = DaysOfOperation::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("operation_day_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Trips::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("trip_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["operation_day_id", "trip_id"],
    indices = [Index(
        name = "idx_trip_days_of_operation_fk_trip_days_of_operation_trip",
        unique = false,
        value = ["trip_id"]
    )]
)
data class TripDaysOfOperation(
    @ColumnInfo(name = "operation_day_id") val operationDayId: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
)
