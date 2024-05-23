package com.baltajmn.sleeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baltajmn.sleeper.domain.Hour
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState

    init {
        viewModelScope.launch {
            delay(2000)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isGetUp = true,
                    hourSelected = "0700",
                    hoursList = generateHoursList(hour = "0700", isGetUp = true)
                )
            }
        }
    }

    fun onGetUpClicked(state: Boolean) {
        val hourSelected = if (_uiState.value.hourSelected.isBlank()) {
            if (state) "0700" else "2000"
        } else {
            _uiState.value.hourSelected
        }
        val hoursList = generateHoursList(hour = hourSelected, isGetUp = state)

        _uiState.update {
            it.copy(
                isGetUp = state,
                hourSelected = hourSelected,
                hoursList = hoursList
            )
        }
    }

    fun onHourSelected(hour: String) =
        _uiState.update {
            it.copy(
                hourSelected = hour,
                hoursList = generateHoursList(hour = hour, isGetUp = it.isGetUp)
            )
        }

    private fun generateHoursList(hour: String, isGetUp: Boolean): List<Hour> {
        if (hour.length != 4) return emptyList()

        val formatter = DateTimeFormatter.ofPattern("HHmm")
        val time = LocalTime.parse(hour, formatter)

        val times = if (isGetUp) {
            listOf(
                Hour(
                    hour = time.minusHours(4).minusMinutes(30).format(formatter),
                    hourSleeping = "4h30",
                    cycles = "3",
                    isRecommended = false
                ),
                Hour(
                    hour = time.minusHours(6).format(formatter),
                    hourSleeping = "6h",
                    cycles = "4",
                    isRecommended = false
                ),
                Hour(
                    hour = time.minusHours(7).minusMinutes(30).format(formatter),
                    hourSleeping = "7h30",
                    cycles = "5",
                    isRecommended = true
                ),
                Hour(
                    hour = time.minusHours(9).format(formatter),
                    hourSleeping = "9h",
                    cycles = "6",
                    isRecommended = true
                )
            )
        } else {
            listOf(
                Hour(
                    hour = time.plusHours(4).plusMinutes(30).format(formatter),
                    hourSleeping = "4h30",
                    cycles = "3",
                    isRecommended = false
                ),
                Hour(
                    hour = time.plusHours(6).format(formatter),
                    hourSleeping = "6h",
                    cycles = "4",
                    isRecommended = false
                ),
                Hour(
                    hour = time.plusHours(7).plusMinutes(30).format(formatter),
                    hourSleeping = "7h30",
                    cycles = "5",
                    isRecommended = true
                ),
                Hour(
                    hour = time.plusHours(9).format(formatter),
                    hourSleeping = "9h",
                    cycles = "6",
                    isRecommended = true
                )
            )
        }

        return times.reversed()
    }
}

data class MainState(
    val isLoading: Boolean = true,
    val isGetUp: Boolean = true,
    val hourSelected: String = "0700",
    val hoursList: List<Hour> = emptyList()
)