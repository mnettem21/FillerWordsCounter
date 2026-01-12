package com.example.fillerwordscounter.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun today(): String = LocalDate.now().format(dateFormatter)

    fun yesterday(): String = LocalDate.now().minusDays(1).format(dateFormatter)

    fun formatDate(date: LocalDate): String = date.format(dateFormatter)

    fun parseDate(dateString: String): LocalDate = LocalDate.parse(dateString, dateFormatter)

    fun getLastNDays(n: Int): Pair<String, String> {
        val end = LocalDate.now()
        val start = end.minusDays((n - 1).toLong())
        return Pair(start.format(dateFormatter), end.format(dateFormatter))
    }

    fun getWeekRange(): Pair<String, String> = getLastNDays(7)

    fun getMonthRange(): Pair<String, String> = getLastNDays(30)

    fun getYearRange(): Pair<String, String> = getLastNDays(365)

    fun getAllTimeRange(): Pair<String, String> {
        // Return a very early date to today
        val start = LocalDate.of(2020, 1, 1)
        val end = LocalDate.now()
        return Pair(start.format(dateFormatter), end.format(dateFormatter))
    }

    fun getDaysInRange(startDate: String, endDate: String): List<String> {
        val start = parseDate(startDate)
        val end = parseDate(endDate)
        val days = mutableListOf<String>()
        var current = start
        while (!current.isAfter(end)) {
            days.add(formatDate(current))
            current = current.plusDays(1)
        }
        return days
    }
}
