package com.submission.tesapp.api

import com.submission.tesapp.data.response.AprioriData
import com.submission.tesapp.data.response.AprioriResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("apriori")
    suspend fun getApriori(
        @Body aprioriData: AprioriData
    ) : Response<AprioriResponse>
}