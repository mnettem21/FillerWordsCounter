package com.example.fillerwordscounter.stt

import android.util.Log
import com.example.fillerwordscounter.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class OpenAiWhisperClient {

    private val client = OkHttpClient()

    fun transcribeWavBlocking(wavBytes: ByteArray): String? {
        val apiKey = BuildConfig.OPENAI_API_KEY
        if (apiKey.isBlank()) {
            Log.e("OpenAI", "OPENAI_API_KEY is blank. Did you set local.properties?")
            return null
        }

        val wavBody = wavBytes.toRequestBody("audio/wav".toMediaType())

        val multipart = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("model", "whisper-1")
            .addFormDataPart("file", "chunk.wav", wavBody)
            .build()

        val req = Request.Builder()
            .url("https://api.openai.com/v1/audio/transcriptions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(multipart)
            .build()

        client.newCall(req).execute().use { resp ->
            val bodyStr = resp.body?.string()
            if (!resp.isSuccessful) {
                Log.e("OpenAI", "HTTP ${resp.code} body=$bodyStr")
                return null
            }
            if (bodyStr.isNullOrBlank()) return null

            // Default response includes "text"
            return JSONObject(bodyStr).optString("text", null)
        }
    }
}