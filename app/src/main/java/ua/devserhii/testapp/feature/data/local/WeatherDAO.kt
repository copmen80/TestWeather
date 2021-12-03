package ua.devserhii.testapp.feature.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ua.devserhii.testapp.feature.data.local.dto.CityTemperatureDto
import ua.devserhii.testapp.feature.data.remote.model.WeatherResponse


@Dao
interface WeatherDAO {
    @Insert
    suspend fun insertAll(vararg cityTemperatureDto: CityTemperatureDto)

    @Delete
    suspend fun delete(cityTemperatureDto: Array<out CityTemperatureDto>)

    @Query("SELECT * FROM temperature WHERE name LIKE :name")
    suspend fun getCityByName(name: String?): CityTemperatureDto?
}