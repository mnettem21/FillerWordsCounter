package com.example.fillerwordscounter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyWordStatsDao {

    @Query("SELECT * FROM daily_word_stats WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): DailyWordStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stats: DailyWordStats)

    @Transaction
    suspend fun incrementForDate(
        date: String,
        addTotalWords: Int,
        addLike: Int,
        addUm: Int,
        addBasically: Int,
        nowMs: Long
    ) {
        val existing = getByDate(date)
        if (existing == null) {
            upsert(
                DailyWordStats(
                    date = date,
                    totalWords = addTotalWords,
                    likeCount = addLike,
                    umCount = addUm,
                    basicallyCount = addBasically,
                    updatedAtEpochMs = nowMs
                )
            )
        } else {
            upsert(
                existing.copy(
                    totalWords = existing.totalWords + addTotalWords,
                    likeCount = existing.likeCount + addLike,
                    umCount = existing.umCount + addUm,
                    basicallyCount = existing.basicallyCount + addBasically,
                    updatedAtEpochMs = nowMs
                )
            )
        }
    }

    @Query("SELECT * FROM daily_word_stats ORDER BY date DESC LIMIT 30")
    fun observeLast30Days(): Flow<List<DailyWordStats>>

    @Query("SELECT * FROM daily_word_stats WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): kotlinx.coroutines.flow.Flow<DailyWordStats?>
}

