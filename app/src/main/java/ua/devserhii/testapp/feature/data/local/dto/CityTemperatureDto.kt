package ua.devserhii.testapp.feature.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "temperature")
data class CityTemperatureDto(
    @PrimaryKey
    val name: String,
    val temperature: Double
)