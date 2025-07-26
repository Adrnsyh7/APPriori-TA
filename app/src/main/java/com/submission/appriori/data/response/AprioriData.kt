package com.submission.appriori.data.response

data class AprioriData(
    val data: List<List<String>>,
    val support: Double,
    val conf: Double
)
