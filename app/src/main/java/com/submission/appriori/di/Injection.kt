package com.submission.appriori.di

import com.submission.appriori.api.*

import com.submission.appriori.data.repository.AprioriRepository
import com.submission.appriori.utils.GateApi


object Injection {
    fun aprioriRepository() : AprioriRepository {
        val gateApi = GateApi.API1
        val apiService =ApiConfig.getApiService(gateApi)
        return AprioriRepository.getInstance(apiService)
    }

}
