package ua.devserhii.testapp.feature.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ua.devserhii.testapp.common.API_KEY
import ua.devserhii.testapp.common.UNITS
import ua.devserhii.testapp.feature.data.remote.model.WeatherResponse

interface TemperatureService {
    @GET("data/2.5/weather")
    suspend fun weatherRequestByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = UNITS
    ): WeatherResponse

    @GET("data/2.5/weather")
    suspend fun weatherRequestByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = UNITS
    ): WeatherResponse
}