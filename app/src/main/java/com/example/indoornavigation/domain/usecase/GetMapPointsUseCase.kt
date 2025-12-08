package com.example.indoornavigation.domain.usecase

import com.example.indoornavigation.data.model.MapPoint
import com.example.indoornavigation.data.repository.MapRepository
import javax.inject.Inject

class GetMapPointsUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend fun execute(): List<MapPoint> {
        return mapRepository.getMapPoints()
    }
}
