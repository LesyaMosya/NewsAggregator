package com.example.newsaggregator.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
fun calculateCountOfHours(dcDate: String): String {
    val date = Instant.parse(dcDate)
    val today = Instant.now()

    var diff = ChronoUnit.DAYS.between(date, today)
    lateinit var diffStr : String

    when {
        (diff < 1) -> {
            diff = ChronoUnit.HOURS.between(date, today)
            diffStr = diff.toString()+"h ago"
        }
        else -> {
            diffStr = diff.toString()+"d ago"
        }
    }

    return if (diff > 0) diffStr
    else "Now"
}


fun String.reduction(replaced: String, toReplace: String): String {
    return if (this != "") this.replace(replaced, toReplace) else "Ph: unknown"
}