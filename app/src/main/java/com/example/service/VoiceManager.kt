package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.data.BotState
import com.example.data.VoiceConfig
import java.util.Locale

class VoiceManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isSpeaking = false
    private var onSpeakingStateChange: ((Boolean) -> Unit)? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("VoiceManager", "Error initializing TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.US
        } else {
            Log.e("VoiceManager", "TTS initialization failed: $status")
        }
    }

    fun setSpeakingListener(listener: (Boolean) -> Unit) {
        this.onSpeakingStateChange = listener
    }

    fun speak(botState: BotState, config: VoiceConfig) {
        if (!config.enabled || !isInitialized || tts == null) return

        val textToSpeak = when (config.language) {
            "Swahili" -> when (botState) {
                BotState.BUYING -> "Dhahabu inanunuliwa, kura nukta tano"
                BotState.BLOCKED -> "Biashara imezuiwa, wigo mkubwa"
                BotState.NEWS_PAUSE -> "Habari zimefika, roboti imesimama"
                BotState.ANALYZING -> "Inachambua soko sasa"
                BotState.SLEEPING -> "Tunasubiri M tano"
            }
            "Luganda" -> when (botState) {
                BotState.BUYING -> "Tugula ezaabu, ebyamaguzi bittukidde"
                BotState.BLOCKED -> "Omulimu gusibiddwa, emiwendo gisusse"
                BotState.NEWS_PAUSE -> "Amawulire gazze, boti eyimiridde"
                BotState.ANALYZING -> "Tukebere akatale kati"
                BotState.SLEEPING -> "Tulindirira akaseera ka M tano"
            }
            else -> when (botState) {
                BotState.BUYING -> "Buy taken on Gold, lot 0.05"
                BotState.BLOCKED -> "Trade blocked, spread too high"
                BotState.NEWS_PAUSE -> "News in 20 mins, paused"
                BotState.ANALYZING -> "Checking EMA20 cross"
                BotState.SLEEPING -> "Waiting for M5 close"
            }
        }

        // Adjust voice tone (male vs female pitch)
        tts?.setPitch(if (config.isFemale) 1.25f else 0.85f)
        tts?.setSpeechRate(1.05f)

        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "cleangold_alert")
        onSpeakingStateChange?.invoke(true)

        // Reset speaking indicator after short duration
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            onSpeakingStateChange?.invoke(false)
        }, 2200)
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("VoiceManager", "Error shutting down TTS", e)
        }
    }
}
