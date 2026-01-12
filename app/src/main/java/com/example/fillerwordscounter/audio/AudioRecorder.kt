package com.example.fillerwordscounter.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlin.math.sqrt

class AudioRecorder {

    private val sampleRate = 16000
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private val audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    @Volatile
    private var isRecording = false

    fun start() {
        audioRecord.startRecording()
        isRecording = true
        Log.d("AudioRecorder", "Recording started")
    }

    fun stop() {
        isRecording = false
        audioRecord.stop()
        audioRecord.release()
        Log.d("AudioRecorder", "Recording stopped")
    }

    fun recordLoop(onChunkFinalized: (ByteArray) -> Unit) {
        val buffer = ShortArray(bufferSize)

        val vad = VadDetector(
            speechRmsThreshold = 500.0, // tune this
            minSpeechMsToStart = 50
        )
        val chunkBuffer = ChunkBuffer(
            sampleRate = sampleRate,
            silenceFinalizeMs = 1500,
            maxChunkMs = 20_000
        )

        var lastRmsLogTime = 0L

        while (isRecording) {
            val read = audioRecord.read(buffer, 0, buffer.size)
            if (read <= 0) continue

            val frameMs = ((read.toDouble() / sampleRate) * 1000.0).toLong().coerceAtLeast(1)
            val rms = computeRms(buffer, read)
            val speech = vad.isSpeech(rms, frameMs)

            val now = System.currentTimeMillis()
            if (now - lastRmsLogTime > 1000) {
                Log.d("AudioRecorder", "RMS=$rms speech=$speech")
                lastRmsLogTime = now
            }

            val chunk = chunkBuffer.onFrame(buffer, read, frameMs, speech)
            if (chunk != null && chunk.isNotEmpty()) {
                onChunkFinalized(chunk)
            }
        }
    }

    private fun computeRms(buffer: ShortArray, read: Int): Double {
        var sum = 0.0
        for (i in 0 until read) {
            sum += buffer[i] * buffer[i]
        }
        return sqrt(sum / read)
    }
}