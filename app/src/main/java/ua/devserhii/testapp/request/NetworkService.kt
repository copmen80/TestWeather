package ua.devserhii.testapp.request

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ua.devserhii.testapp.feature.data.remote.model.WeatherResponse


interface NetworkService {
    @GET("data/2.5/weather")
    fun weatherRequestByCityName(
        @Query("q") cityName: String,
        @Query("appid") ApiKey: String
    ): Observable<WeatherResponse>

    @GET("data/2.5/weather")
    fun weatherRequestByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") ApiKey: String
    ): Observable<WeatherResponse>

}