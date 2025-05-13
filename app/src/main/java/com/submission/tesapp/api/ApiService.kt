package com.submission.tesapp.api

import com.submission.tesapp.data.response.AprioriData
import com.submission.tesapp.data.response.AprioriResponses
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("apriori")
    suspend fun getApriori(
        @Body requestBody: AprioriData
    ) : Response<AprioriResponses>
}