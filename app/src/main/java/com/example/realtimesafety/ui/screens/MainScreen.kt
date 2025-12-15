package com.example.realtimesafety.ui.screens

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realtimesafety.ui.MainViewModel
import com.example.realtimesafety.util.CoordinateTransformer

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onInitializeArCore: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isServiceBound by viewModel.isServiceBound.collectAsState()
    val surfaceProvider by viewModel.surfaceProvider.collectAsState()
    val context = LocalContext.current
    var screenWidth by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }

    val coordinateTransformer = remember(uiState.imageWidth, uiState.imageHeight, screenWidth, screenHeight) {
        CoordinateTransformer(uiState.imageWidth, uiState.imageHeight, screenWidth, screenHeight)
    }

    LaunchedEffect(Unit) {
        viewModel.bindService(context)
    }

    LaunchedEffect(isServiceBound) {
        if (isServiceBound) {
            onInitializeArCore()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.unbindService(context)
        }
    }

    Box(modifier = Modifier.fillMaxSize().onSizeChanged {
        screenWidth = it.width
        screenHeight = it.height
    }) {
        surfaceProvider?.let { provider ->
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    provider.setSurfaceProvider(previewView.surfaceProvider)
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            uiState.trackedObjects.forEach {
                val transformedRect = coordinateTransformer.transform(it.boundingBox)
                drawRect(
                    color = Color.Red,
                    topLeft = transformedRect.topLeft,
                    size = transformedRect.size,
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}
