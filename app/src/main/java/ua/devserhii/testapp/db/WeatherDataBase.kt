package ua.devserhii.testapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.devserhii.testapp.feature.data.local.WeatherDAO
import ua.devserhii.testapp.feature.data.local.dto.CityTemperatureDto

@Database(entities = [CityTemperatureDto::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDAO?
}