package com.example.fillerwordscounter.stt

import java.nio.ByteBuffer
import java.nio.ByteOrder

object WavUtil {
    fun pcm16leToWav(
        pcmData: ByteArray,
        sampleRate: Int = 16000,
        numChannels: Short = 1,
        bitsPerSample: Short = 16
    ): ByteArray {
        val byteRate = sampleRate * numChannels * (bitsPerSample / 8)
        val blockAlign: Short = (numChannels * (bitsPerSample / 8)).toShort()
        val dataSize = pcmData.size
        val riffChunkSize = 36 + dataSize

        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray())
        header.putInt(riffChunkSize)
        header.put("WAVE".toByteArray())
        header.put("fmt ".toByteArray())
        header.putInt(16) // PCM subchunk size
        header.putShort(1) // AudioFormat PCM = 1
        header.putShort(numChannels)
        header.putInt(sampleRate)
        header.putInt(byteRate)
        header.putShort(blockAlign)
        header.putShort(bitsPerSample)
        header.put("data".toByteArray())
        header.putInt(dataSize)

        return header.array() + pcmData
    }
}