package ua.devserhii.testapp.feature.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main")
    val temperature: Temperature,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,
)
