package com.example.indoornavigation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indoornavigation.domain.usecase.GetMapPointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMapPointsUseCase: GetMapPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(emptyList<com.example.indoornavigation.data.model.MapPoint>())
    val state: StateFlow<List<com.example.indoornavigation.data.model.MapPoint>> = _state

    fun loadMapPoints() {
        viewModelScope.launch {
            _state.value = getMapPointsUseCase.execute()
        }
    }
}
