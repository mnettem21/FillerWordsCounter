package com.example.fillerwordscounter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fillerwordscounter.data.AppDatabase
import com.example.fillerwordscounter.data.DailyWordStats
import com.example.fillerwordscounter.ui.filters.RangeFilter
import com.example.fillerwordscounter.ui.filters.WordFilter
import com.example.fillerwordscounter.utils.DateUtils
import kotlinx.coroutines.flow.*

data class TrendsUiState(
    val rangeFilter: RangeFilter,
    val wordFilter: WordFilter,
    val stats: List<DailyWordStats>,
    val totalFillersInRange: Int,
    val averageFillerPercentage: Float
)

class TrendsViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).dailyWordStatsDao()

    private val rangeFilterFlow = MutableStateFlow(RangeFilter.Week)
    private val wordFilterFlow = MutableStateFlow(WordFilter.All)

    val uiState: StateFlow<TrendsUiState> = combine(
        rangeFilterFlow,
        wordFilterFlow
    ) { rangeFilter, wordFilter ->
        val (startDate, endDate) = when (rangeFilter) {
            RangeFilter.Week -> DateUtils.getWeekRange()
            RangeFilter.Month -> DateUtils.getMonthRange()
            RangeFilter.Year -> DateUtils.getYearRange()
            RangeFilter.AllTime -> DateUtils.getAllTimeRange()
        }

        Pair(dao.observeRange(startDate, endDate), wordFilter)
    }.flatMapLatest { (statsFlow, wordFilter) ->
        statsFlow.map { stats ->
            val filteredStats = stats.sortedBy { it.date }

            val totalFillersInRange = filteredStats.sumOf { stat ->
                when (wordFilter) {
                    WordFilter.All -> stat.likeCount + stat.umCount + stat.basicallyCount
                    WordFilter.Like -> stat.likeCount
                    WordFilter.Um -> stat.umCount
                    WordFilter.Basically -> stat.basicallyCount
                }
            }

            val totalWords = filteredStats.sumOf { it.totalWords }
            val averageFillerPercentage = if (totalWords > 0) {
                (totalFillersInRange.toFloat() / totalWords) * 100f
            } else 0f

            TrendsUiState(
                rangeFilter = rangeFilterFlow.value,
                wordFilter = wordFilterFlow.value,
                stats = filteredStats,
                totalFillersInRange = totalFillersInRange,
                averageFillerPercentage = averageFillerPercentage
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TrendsUiState(
            rangeFilter = RangeFilter.Week,
            wordFilter = WordFilter.All,
            stats = emptyList(),
            totalFillersInRange = 0,
            averageFillerPercentage = 0f
        )
    )

    fun setRangeFilter(filter: RangeFilter) {
        rangeFilterFlow.value = filter
    }

    fun setWordFilter(filter: WordFilter) {
        wordFilterFlow.value = filter
    }
}
