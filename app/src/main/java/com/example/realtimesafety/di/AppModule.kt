package com.example.realtimesafety.di

import android.content.Context
import com.example.realtimesafety.audio.AudioEngine
import com.example.realtimesafety.ar.DepthManager
import com.example.realtimesafety.domain.HazardEngine
import com.example.realtimesafety.ml.ObjectDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideObjectDetector(@ApplicationContext context: Context): ObjectDetector {
        return ObjectDetector(context)
    }

    @Provides
    @Singleton
    fun provideDepthManager(): DepthManager {
        return DepthManager()
    }

    @Provides
    @Singleton
    fun provideHazardEngine(): HazardEngine {
        return HazardEngine()
    }

    @Provides
    @Singleton
    fun provideAudioEngine(@ApplicationContext context: Context): AudioEngine {
        return AudioEngine(context)
    }
}
