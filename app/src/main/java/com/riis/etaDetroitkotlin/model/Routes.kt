package com.riis.etaDetroitkotlin.model

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Company::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("companyId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
    ],
    indices = [Index(value = ["company_id", "route_number"], unique = true)],
    tableName = "routes"
)

data class Routes(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "route_number") val number: Int,
    @ColumnInfo(name = "company_id") val companyId: Int,
    @ColumnInfo(name = "route_name") val name: String = "",
    @ColumnInfo(name = "route_description") val description: String = ""
)
