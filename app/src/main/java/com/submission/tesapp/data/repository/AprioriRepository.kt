package com.submission.tesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.submission.tesapp.api.ApiService
import com.submission.tesapp.data.ResultState
import com.submission.tesapp.data.response.AprioriData
import com.submission.tesapp.data.response.AprioriResponses

class AprioriRepository(private val apiService: ApiService) {
    fun processApriori(data: AprioriData): LiveData<ResultState<AprioriResponses>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getApriori(data)
            if(response.isSuccessful) {
                val aprioriResResponse = response.body()?.data
                if(aprioriResResponse !=null) {
                    val aprioriResponse = AprioriResponses(aprioriResResponse)
                    emit(ResultState.Success(aprioriResponse))
                } else {
                    emit(ResultState.Error("Response body data is null"))
                }
            } else {
                emit(ResultState.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            ResultState.Error("Error: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: AprioriRepository? = null



        fun getInstance(apiService: ApiService): AprioriRepository {
            return instance ?: synchronized(this) {
                instance ?: AprioriRepository(apiService)
            }.also { instance = it }
        }
    }
}