package com.submission.tesapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateConvert {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDate(currentDate: Date?) : String {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}