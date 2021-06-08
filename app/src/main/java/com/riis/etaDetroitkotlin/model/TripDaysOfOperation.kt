package com.riis.etaDetroitkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

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
    primaryKeys = ["operation_day_id", "trip_id"]
)
data class TripDaysOfOperation(
    @ColumnInfo(name = "operation_day_id") val operationDayId: Int,
    @ColumnInfo(name = "trip_id") val tripId: Int,
)
