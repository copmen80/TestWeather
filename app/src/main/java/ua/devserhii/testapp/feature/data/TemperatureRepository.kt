package ua.devserhii.testapp.feature.data

import ua.devserhii.testapp.feature.data.local.WeatherDAO
import ua.devserhii.testapp.feature.data.local.dto.CityTemperatureDto
import ua.devserhii.testapp.feature.data.remote.TemperatureService
import ua.devserhii.testapp.feature.data.remote.model.WeatherResponse
import javax.inject.Inject

class TemperatureRepository @Inject constructor(
    private val weatherDAO: WeatherDAO,
    private val temperatureService: TemperatureService,
) {
    suspend fun getWeatherByCityName(cityName: String): CityTemperatureDto {
        val localWeather = weatherDAO.getCityByName(cityName)
        return if (localWeather != null) {
            localWeather
        } else {
            val remoteWeather = temperatureService.weatherRequestByCityName(cityName).toDto()
            weatherDAO.insertAll(remoteWeather)
            remoteWeather
        }
    }

    suspend fun getWeatherByCoordinates(
        latitude: String,
        longitude: String,
    ): CityTemperatureDto {
        val remoteWeather =
            temperatureService.weatherRequestByCoordinates(latitude, longitude).toDto()
        weatherDAO.insertAll(remoteWeather)
        return remoteWeather
    }

    private fun WeatherResponse.toDto(): CityTemperatureDto {
        return CityTemperatureDto(name, temperature.temp)
    }


}