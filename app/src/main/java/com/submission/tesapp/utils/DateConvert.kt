package com.submission.tesapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateConvert {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDate(currentDate: Date?) : String {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }
    fun convertTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }
}