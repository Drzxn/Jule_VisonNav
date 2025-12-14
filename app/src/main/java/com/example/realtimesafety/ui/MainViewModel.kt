package com.example.realtimesafety.ui

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.camera.core.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimesafety.data.UIState
import com.example.realtimesafety.service.ProcessingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UIState(emptyList(), 1, 1))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private var processingService: ProcessingService? = null
    private val _isServiceBound = MutableStateFlow(false)
    val isServiceBound: StateFlow<Boolean> = _isServiceBound.asStateFlow()

    private val _surfaceProvider = MutableStateFlow<Preview.SurfaceProvider?>(null)
    val surfaceProvider: StateFlow<Preview.SurfaceProvider?> = _surfaceProvider.asStateFlow()


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ProcessingService.LocalBinder
            processingService = binder.getService()
            _isServiceBound.value = true
            viewModelScope.launch {
                processingService?.uiState?.collect {
                    _uiState.value = it
                }
            }
            viewModelScope.launch {
                processingService?.surfaceProvider?.collect {
                    _surfaceProvider.value = it
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            processingService = null
            _isServiceBound.value = false
        }
    }

    fun initializeArCore(activity: Activity) {
        processingService?.initializeArCore(activity)
    }

    fun bindService(context: Context) {
        Intent(context, ProcessingService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService(context: Context) {
        if (_isServiceBound.value) {
            context.unbindService(serviceConnection)
            _isServiceBound.value = false
        }
    }
}
