package com.example.indoornavigation.di

import android.content.Context
import androidx.room.Room
import com.example.indoornavigation.data.database.AppDatabase
import com.example.indoornavigation.data.database.MapPointDao
import com.example.indoornavigation.data.repository.MapRepository
import com.example.indoornavigation.data.repository.MapRepositoryImpl
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "indoor_navigation_db"
        ).build()
    }

    @Provides
    fun provideMapPointDao(appDatabase: AppDatabase): MapPointDao {
        return appDatabase.mapPointDao()
    }

    @Provides
    @Singleton
    fun provideMapRepository(mapPointDao: MapPointDao): MapRepository {
        return MapRepositoryImpl(mapPointDao)
    }
}
