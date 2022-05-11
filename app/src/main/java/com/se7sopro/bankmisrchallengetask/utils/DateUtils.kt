package com.se7sopro.bankmisrchallengetask.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDate(): String {
        val calendar: Date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar)
    }
}