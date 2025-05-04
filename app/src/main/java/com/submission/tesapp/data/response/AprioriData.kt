package com.submission.tesapp.data.response

data class AprioriData(
    val data: Map<String, List<String?>>,

    val support: Double,
    val confidence: Double
)
