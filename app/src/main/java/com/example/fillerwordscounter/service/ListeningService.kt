package com.example.fillerwordscounter.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.fillerwordscounter.audio.AudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import com.example.fillerwordscounter.stt.OpenAiWhisperClient
import com.example.fillerwordscounter.stt.WavUtil
import com.example.fillerwordscounter.data.AppDatabase
import com.example.fillerwordscounter.processing.WordCounter
import java.time.LocalDate

class ListeningService : Service() {

    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var recorder: AudioRecorder? = null

    private val chunkChannel = Channel<ByteArray>(capacity = Channel.BUFFERED)
    private val whisper = OpenAiWhisperClient()

    private val dao by lazy { AppDatabase.get(this).dailyWordStatsDao() }

    override fun onCreate() {
        super.onCreate()
        ServiceNotification.createChannel(this)
        Log.d("ListeningService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ListeningService", "onStartCommand")

        val notification = ServiceNotification.buildNotification(this)
        startForeground(ServiceNotification.NOTIFICATION_ID, notification)

        if (recorder == null) {
            recorder = AudioRecorder().also { it.start() }
            Log.d("ListeningService", "Recorder started; launching producer/consumer")

            // Producer: audio → chunks
            serviceScope.launch {
                recorder?.recordLoop { pcmChunkBytes ->
                    val result = chunkChannel.trySend(pcmChunkBytes)
                    if (result.isSuccess) {
                        Log.d("ListeningService", "Queued chunk bytes=${pcmChunkBytes.size}")
                    } else {
                        Log.w("ListeningService", "Dropped chunk: channel closed/full (size=${pcmChunkBytes.size})")
                    }
                }
            }

            // Consumer: chunks → OpenAI (sequential)
            serviceScope.launch {
                for (pcmChunkBytes in chunkChannel) {
                    if (pcmChunkBytes.size < 16_000) {
                        Log.d("WhisperSTT", "Skipping tiny chunk: bytes=${pcmChunkBytes.size}")
                        continue
                    }
                    val wav = WavUtil.pcm16leToWav(
                        pcmData = pcmChunkBytes,
                        sampleRate = 16000,
                        numChannels = 1,
                        bitsPerSample = 16
                    )

                    val text = whisper.transcribeWavBlocking(wav)
                    if (!text.isNullOrBlank()) {
                        val counts = WordCounter.count(text)

                        val today = LocalDate.now().toString() // "YYYY-MM-DD"
                        dao.incrementForDate(
                            date = today,
                            addTotalWords = counts.totalWords,
                            addLike = counts.likeCount,
                            addUm = counts.umCount,
                            addBasically = counts.basicallyCount,
                            nowMs = System.currentTimeMillis()
                        )

                        Log.d(
                            "Counts",
                            "date=$today +words=${counts.totalWords} +like=${counts.likeCount} +um=${counts.umCount} +basically=${counts.basicallyCount}"
                        )

                        // We intentionally do NOT store `text` anywhere.
                    } else {
                        Log.w("WhisperSTT", "No transcript returned (short audio or API error)")
                    }
                }
            }
        } else {
            Log.d("ListeningService", "Recorder already running")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("ListeningService", "onDestroy")
        chunkChannel.close()
        recorder?.stop()
        recorder = null
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}