package com.rodgim.movies.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {

    fun format(date: Date, format: String) = apply(date, format)

    fun format(timestamp: Long, format: String) = apply(Date(timestamp), format)

    fun now(format: String) = apply(Calendar.getInstance().time, format)

    fun now() = Calendar.getInstance().time

    fun formatCurrentTime() = SimpleDateFormat("hh:mm a", Locale.US).format(now())

    fun parse(timestamp: Long) = Date(timestamp)

    fun parse(source: String, format: String) = SimpleDateFormat(format, Locale.getDefault()).parse(source)

    fun formatDate(date: String): String = SimpleDateFormat("d MMMM", Locale.US).format(DateUtils().parse(date, FORMAT_DATE6)!!)

    fun formatDate(date: Date): String = SimpleDateFormat("d MMMM", Locale.US).format(date)

    fun formatTime(date: String): String = SimpleDateFormat("hh:mm a", Locale.US).format(DateUtils().parse(date, FORMAT_DATE6)!!)

    fun millisecondsSince(date: String): Long {
        val dateSince = parse(date, FORMAT_DATE6)!!
        return dateSince.time
    }

    fun minutesSince(date: String): Long {
        val diff = now().time - millisecondsSince(date)
        val seconds = diff / 1000
        return seconds / 60
    }

    private fun apply(date: Date, format: String): String {
        val simpleDateFormatNew = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormatNew.format(date)
    }

    fun getTimeAgo(now: Long, time: Long): String {
        var time = time
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        if (time > now || time <= 0) {
            return "just now"
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> {
                "just now"
            }
            diff < 2 * MINUTE_MILLIS -> {
                "a minute ago"
            }
            diff < 50 * MINUTE_MILLIS -> {
                (diff / MINUTE_MILLIS).toString() + " minutes ago"
            }
            diff < 90 * MINUTE_MILLIS -> {
                "an hour ago"
            }
            diff < 24 * HOUR_MILLIS -> {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            }
            diff < 48 * HOUR_MILLIS -> {
                "yesterday"
            }
            diff < MONTH_MILLIS -> {
                if (diff / DAY_MILLIS == 1L)
                    (diff / DAY_MILLIS).toString() + " day ago"
                else
                    (diff / DAY_MILLIS).toString() + " days ago"
            }
            diff < YEAR_MILLIS -> {
                if (diff / MONTH_MILLIS == 1L)
                    (diff / MONTH_MILLIS).toString() + " month ago"
                else
                    (diff / MONTH_MILLIS).toString() + " months ago"
            }
            else -> {
                if (diff / YEAR_MILLIS == 1L)
                    (diff / YEAR_MILLIS).toString() + " year ago"
                else
                    (diff / YEAR_MILLIS).toString() + " years ago"
            }
        }
    }

    companion object {
        const val FORMAT_TIME1 = "HH:mm"
        const val FORMAT_TIME2 = "HH:mm:ss"
        const val FORMAT_DATE1 = "yyyy-MM-dd"
        const val FORMAT_DATE2 = "dd/MM/yyyy"
        const val FORMAT_DATE3 = "dd/MM/yyyy HH:mm"
        const val FORMAT_DATE4 = "dd/MM/yyyy HH:mm:ss"
        const val FORMAT_DATE5 = "yyyy-MM-dd HH:mm"
        const val FORMAT_DATE6 = "yyyy-MM-dd'T'HH:mm:ss"

        private val SECOND_MILLIS = 1000L
        private val MINUTE_MILLIS = 60L * SECOND_MILLIS
        private val HOUR_MILLIS = 60L * MINUTE_MILLIS
        private val DAY_MILLIS = 24L * HOUR_MILLIS
        private val MONTH_MILLIS = 30L * DAY_MILLIS
        private val YEAR_MILLIS = 365L * DAY_MILLIS
    }
}
