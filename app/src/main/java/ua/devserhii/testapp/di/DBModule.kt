package ua.devserhii.testapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.devserhii.testapp.db.WeatherDataBase
import ua.devserhii.testapp.feature.data.local.WeatherDAO

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): WeatherDataBase {
        return Room.databaseBuilder(
            context,
            WeatherDataBase::class.java, "weather-database"
        ).build()
    }

    @Provides
    fun provideWeatherDAO(weatherDataBase: WeatherDataBase): WeatherDAO {
        return requireNotNull(weatherDataBase.getWeatherDao())
    }
}