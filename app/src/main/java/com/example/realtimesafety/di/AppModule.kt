package com.example.realtimesafety.di

import android.content.Context
import com.example.realtimesafety.audio.AudioEngine
import com.example.realtimesafety.audio.AudioPriorityManager
import com.example.realtimesafety.ar.DepthManager
import com.example.realtimesafety.domain.DepthSmoother
import com.example.realtimesafety.domain.HazardEvaluator
import com.example.realtimesafety.domain.MotionEstimator
import com.example.realtimesafety.domain.ObjectTracker
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
    fun provideObjectTracker(): ObjectTracker {
        return ObjectTracker()
    }

    @Provides
    @Singleton
    fun provideDepthSmoother(): DepthSmoother {
        return DepthSmoother()
    }

    @Provides
    @Singleton
    fun provideMotionEstimator(): MotionEstimator {
        return MotionEstimator()
    }

    @Provides
    @Singleton
    fun provideHazardEvaluator(): HazardEvaluator {
        return HazardEvaluator()
    }

    @Provides
    @Singleton
    fun provideAudioEngine(@ApplicationContext context: Context): AudioEngine {
        return AudioEngine(context)
    }

    @Provides
    @Singleton
    fun provideAudioPriorityManager(audioEngine: AudioEngine): AudioPriorityManager {
        return AudioPriorityManager(audioEngine)
    }
}
