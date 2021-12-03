package ua.devserhii.testapp.feature.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.devserhii.testapp.feature.data.TemperatureRepository
import ua.devserhii.testapp.feature.data.local.dto.CityTemperatureDto
import javax.inject.Inject


class GetTemperatureByCoordinatesUseCase @Inject constructor(
    private val temperatureRepository: TemperatureRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): CityTemperatureDto {
        return withContext(Dispatchers.IO) {
            temperatureRepository.getWeatherByCoordinates(lat.toString(), lon.toString())
        }
    }
}