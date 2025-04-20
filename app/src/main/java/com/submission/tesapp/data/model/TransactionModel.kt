package com.submission.tesapp.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class TransactionModel(
    val id: String= "", var item: String ?= null, val date: Date?= null
) : Parcelable