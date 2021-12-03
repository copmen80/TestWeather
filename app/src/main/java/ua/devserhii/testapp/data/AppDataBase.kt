package ua.devserhii.testapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.devserhii.testapp.feature.data.local.dto.WeatherDAO
import ua.devserhii.testapp.feature.data.remote.model.WeatherResponse


@Database(entities = [WeatherResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDAO?
}