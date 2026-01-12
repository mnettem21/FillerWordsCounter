package com.example.fillerwordscounter.audio

class VadDetector(
    private val speechRmsThreshold: Double = 120.0, // adjust if needed
    private val minSpeechMsToStart: Long = 200       // avoid false starts
) {
    private var speechMsAccum: Long = 0

    fun isSpeech(rms: Double, frameMs: Long): Boolean {
        // Simple threshold-based VAD with a tiny “must be speech for N ms” gate.
        if (rms >= speechRmsThreshold) {
            speechMsAccum += frameMs
        } else {
            speechMsAccum = 0
        }
        return speechMsAccum >= minSpeechMsToStart
    }
}