package com.example.fillerwordscounter.audio

import java.io.ByteArrayOutputStream

class ChunkBuffer(
    private val sampleRate: Int,
    private val silenceFinalizeMs: Long = 1500,
    private val maxChunkMs: Long = 20_000
) {
    private val out = ByteArrayOutputStream()
    private var inSpeech = false

    private var chunkSpeechMs: Long = 0
    private var silenceMs: Long = 0

    fun onFrame(pcm16: ShortArray, read: Int, frameMs: Long, isSpeech: Boolean): ByteArray? {
        if (isSpeech) {
            if (!inSpeech) {
                // starting a new chunk
                reset()
                inSpeech = true
            }

            silenceMs = 0
            chunkSpeechMs += frameMs
            appendPcm(pcm16, read)

            if (chunkSpeechMs >= maxChunkMs) {
                return finalizeChunk()
            }
        } else {
            if (inSpeech) {
                silenceMs += frameMs

                // Optional: you can append trailing silence too, but not required.
                // appendPcm(pcm16, read)

                if (silenceMs >= silenceFinalizeMs) {
                    return finalizeChunk()
                }
            }
        }

        return null
    }

    private fun appendPcm(pcm16: ShortArray, read: Int) {
        // Convert short PCM to little-endian bytes
        for (i in 0 until read) {
            val s = pcm16[i].toInt()
            out.write(s and 0xFF)
            out.write((s shr 8) and 0xFF)
        }
    }

    private fun finalizeChunk(): ByteArray {
        val bytes = out.toByteArray()
        reset()
        inSpeech = false
        return bytes
    }

    private fun reset() {
        out.reset()
        chunkSpeechMs = 0
        silenceMs = 0
    }
}