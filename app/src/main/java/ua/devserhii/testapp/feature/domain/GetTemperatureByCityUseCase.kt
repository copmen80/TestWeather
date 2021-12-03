package ua.devserhii.testapp.feature.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.devserhii.testapp.feature.data.TemperatureRepository
import ua.devserhii.testapp.feature.data.local.dto.CityTemperatureDto
import javax.inject.Inject

/**
 * Created by Vladislav Boiko on 03.12.2021.
 */
class GetTemperatureByCityUseCase @Inject constructor(
    private val temperatureRepository: TemperatureRepository
) {
    suspend operator fun invoke(cityName: String): CityTemperatureDto {
        return withContext(Dispatchers.IO) {
            temperatureRepository.getWeatherByCityName(cityName)
        }
    }
}