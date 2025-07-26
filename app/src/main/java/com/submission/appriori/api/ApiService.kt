package com.submission.appriori.api

import com.submission.appriori.data.response.AprioriData
import com.submission.appriori.data.response.AprioriResponses
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("apriori")
    suspend fun getApriori(
        @Body requestBody: AprioriData
    ) : Response<AprioriResponses>
}