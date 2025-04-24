package com.submission.tesapp.di

import com.submission.tesapp.api.*
import com.submission.tesapp.data.repository.AprioriRepository
import com.submission.tesapp.utils.GateApi

object Injection {
    fun aprioriRepository() : AprioriRepository {
        val gateApi = GateApi.API1
        val apiService =ApiConfig.getApiService(gateApi)
        return AprioriRepository.getInstance(apiService)
    }
}
