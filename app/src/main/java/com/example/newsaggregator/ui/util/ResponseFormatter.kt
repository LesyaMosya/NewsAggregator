package com.example.newsaggregator.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
fun calculateCountOfHours(dcDate: String): String {
    val date = Instant.parse(dcDate)
    val today = Instant.now()

    return ChronoUnit.HOURS.between(date, today).toString()+"h ago"
}


fun String?.reduction(replaced: String, toReplace: String): String {
    return this?.replace(replaced, toReplace) ?: "Unknown"

}