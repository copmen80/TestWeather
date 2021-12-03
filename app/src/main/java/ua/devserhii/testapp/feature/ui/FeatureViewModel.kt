package ua.devserhii.testapp.feature.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ua.devserhii.testapp.feature.data.local.dto.CityTemperatureDto
import ua.devserhii.testapp.feature.domain.GetTemperatureByCityUseCase
import ua.devserhii.testapp.feature.domain.GetTemperatureByCoordinatesUseCase
import javax.inject.Inject

@HiltViewModel
class FeatureViewModel @Inject constructor(
    application: Application,
    private val getTemperatureByCityUseCase: GetTemperatureByCityUseCase,
    private val getTemperatureByCoordinatesUseCase: GetTemperatureByCoordinatesUseCase
) : AndroidViewModel(application) {

    private val _content = MutableSharedFlow<CityTemperatureDto>()
    val content = _content.asSharedFlow()

    private val eventChannel = Channel<String>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun getTemperatureByCity(cityName: String) {
        viewModelScope.launch {
            try {
                _content.emit(getTemperatureByCityUseCase(cityName))
            } catch (e: Exception) {
                e.printStackTrace()
                eventChannel.send("Fetch Error")
            }
        }
    }

    fun getTemperatureByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _content.emit(getTemperatureByCoordinatesUseCase(lat, lon))
            } catch (e: Exception) {
                e.printStackTrace()
                eventChannel.send("Fetch Error")
            }
        }
    }
}
