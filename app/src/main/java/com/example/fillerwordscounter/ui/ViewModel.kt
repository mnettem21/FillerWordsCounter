package com.example.fillerwordscounter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fillerwordscounter.data.AppDatabase
import com.example.fillerwordscounter.data.DailyWordStats
import com.example.fillerwordscounter.ui.filters.WordFilter
import com.example.fillerwordscounter.utils.DateUtils
import kotlinx.coroutines.flow.*
import java.time.LocalDate

data class TodayUiState(
    val date: String,
    val totalWords: Int,
    val likeCount: Int,
    val umCount: Int,
    val basicallyCount: Int,
    val wordFilter: WordFilter,
    val totalFillerWords: Int,
    val fillerPercentage: Float,
    val dailyAverageFillerCount: Float,
    val deltaVsYesterday: Float? // percentage change, null if no yesterday data
)

class TodayViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).dailyWordStatsDao()
    private val today = DateUtils.today()
    private val yesterday = DateUtils.yesterday()

    private val wordFilterFlow = MutableStateFlow(WordFilter.All)

    val uiState: StateFlow<TodayUiState> = combine(
        dao.observeByDate(today),
        dao.observeByDate(yesterday),
        dao.observeAll(),
        wordFilterFlow
    ) { todayStats, yesterdayStats, allStats, filter ->
        val todayData = todayStats ?: DailyWordStats(today, 0, 0, 0, 0, System.currentTimeMillis())
        val yesterdayData = yesterdayStats

        val totalFillerWords = when (filter) {
            WordFilter.All -> todayData.likeCount + todayData.umCount + todayData.basicallyCount
            WordFilter.Like -> todayData.likeCount
            WordFilter.Um -> todayData.umCount
            WordFilter.Basically -> todayData.basicallyCount
        }

        val fillerPercentage = if (todayData.totalWords > 0) {
            (totalFillerWords.toFloat() / todayData.totalWords) * 100f
        } else 0f

        // Calculate daily average filler count (all-time average)
        val dailyAverageFillerCount = if (allStats.isNotEmpty()) {
            val totalFillers = allStats.sumOf { it.likeCount + it.umCount + it.basicallyCount }
            totalFillers.toFloat() / allStats.size
        } else 0f

        // Calculate delta vs yesterday
        val deltaVsYesterday = yesterdayData?.let { y ->
            val yesterdayFillers = when (filter) {
                WordFilter.All -> y.likeCount + y.umCount + y.basicallyCount
                WordFilter.Like -> y.likeCount
                WordFilter.Um -> y.umCount
                WordFilter.Basically -> y.basicallyCount
            }
            if (yesterdayFillers > 0) {
                ((totalFillerWords - yesterdayFillers).toFloat() / yesterdayFillers) * 100f
            } else if (totalFillerWords > 0) {
                100f // 100% increase from 0
            } else {
                0f
            }
        }

        TodayUiState(
            date = today,
            totalWords = todayData.totalWords,
            likeCount = todayData.likeCount,
            umCount = todayData.umCount,
            basicallyCount = todayData.basicallyCount,
            wordFilter = filter,
            totalFillerWords = totalFillerWords,
            fillerPercentage = fillerPercentage,
            dailyAverageFillerCount = dailyAverageFillerCount,
            deltaVsYesterday = deltaVsYesterday
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState(today, 0, 0, 0, 0, WordFilter.All, 0, 0f, 0f, null)
    )

    fun setWordFilter(filter: WordFilter) {
        wordFilterFlow.value = filter
    }
}
