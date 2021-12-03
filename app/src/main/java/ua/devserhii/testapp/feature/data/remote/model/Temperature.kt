package ua.devserhii.testapp.feature.data.remote.model

import com.google.gson.annotations.SerializedName

data class Temperature(
    @SerializedName("temp")
    val temp: Double,
)