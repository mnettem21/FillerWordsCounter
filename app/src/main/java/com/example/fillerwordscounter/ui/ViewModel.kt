package com.example.fillerwordscounter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fillerwordscounter.data.AppDatabase
import com.example.fillerwordscounter.data.DailyWordStats
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class TodayUiState(
    val date: String,
    val totalWords: Int,
    val likeCount: Int,
    val umCount: Int,
    val basicallyCount: Int
)

class TodayViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).dailyWordStatsDao()
    private val today = LocalDate.now().toString()

    val uiState: StateFlow<TodayUiState> =
        dao.observeByDate(today)
            .map { stats -> stats.toUiState(today) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TodayUiState(today, 0, 0, 0, 0)
            )

    private fun DailyWordStats?.toUiState(date: String): TodayUiState {
        return TodayUiState(
            date = date,
            totalWords = this?.totalWords ?: 0,
            likeCount = this?.likeCount ?: 0,
            umCount = this?.umCount ?: 0,
            basicallyCount = this?.basicallyCount ?: 0
        )
    }
}
