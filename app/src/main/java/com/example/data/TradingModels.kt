package com.example.data

import androidx.compose.ui.graphics.Color

enum class BotState(
    val title: String,
    val statusMessage: String,
    val voiceAlert: String
) {
    SLEEPING(
        title = "STANDBY",
        statusMessage = "Waiting for M5 close...",
        voiceAlert = "Waiting for M5 candle close"
    ),
    ANALYZING(
        title = "ANALYZING",
        statusMessage = "Scanning M5 EMA20/50 cross",
        voiceAlert = "Scanning market for EMA cross"
    ),
    BUYING(
        title = "EXECUTION",
        statusMessage = "Taking BUY, RSI 58, 1% risk",
        voiceAlert = "Buy taken on Gold, lot 0.05, one percent risk locked"
    ),
    BLOCKED(
        title = "PROTECTION",
        statusMessage = "Spread too high, skipping entry",
        voiceAlert = "Trade blocked, spread too high"
    ),
    NEWS_PAUSE(
        title = "PAUSED FOR NEWS",
        statusMessage = "High Impact USD CPI in 18m",
        voiceAlert = "High impact news detected. PovertyScalper paused for news safety."
    )
}

enum class AppThemeColor(
    val id: String,
    val displayName: String,
    val primary: Color,
    val primaryVariant: Color,
    val glow: Color
) {
    GOLD(
        id = "gold",
        displayName = "Gold",
        primary = Color(0xFFFFD700),
        primaryVariant = Color(0xFFE5C100),
        glow = Color(0x66FFD700)
    ),
    ICE_BLUE(
        id = "ice_blue",
        displayName = "Ice Blue",
        primary = Color(0xFF00D4FF),
        primaryVariant = Color(0xFF00B2D6),
        glow = Color(0x6600D4FF)
    ),
    MATRIX_GREEN(
        id = "matrix_green",
        displayName = "Matrix Green",
        primary = Color(0xFF00FF88),
        primaryVariant = Color(0xFF00D672),
        glow = Color(0x6600FF88)
    ),
    ROYAL_PURPLE(
        id = "royal_purple",
        displayName = "Royal Purple",
        primary = Color(0xFF9D00FF),
        primaryVariant = Color(0xFF8200D6),
        glow = Color(0x669D00FF)
    )
}

data class Trade(
    val id: String,
    val account: String,
    val symbol: String = "XAUUSD",
    val lot: Double,
    val type: String, // "BUY" or "SELL"
    val sl: Double,
    val tp: Double,
    val openPrice: Double,
    val closePrice: Double? = null,
    val profit: Double,
    val timestamp: String,
    val isClosed: Boolean = true
)

enum class Mt5VerificationStatus {
    UNLINKED,
    PENDING_VERIFICATION,
    VERIFIED,
    REJECTED
}

data class UserProfile(
    val email: String,
    val mt5Account: String,
    val expiryDate: String,
    val expiryDaysRemaining: Int = 12,
    val expiryHoursRemaining: Int = 4,
    val isActive: Boolean = true,
    val balance: Double = 10000.0,
    val equity: Double = 12450.80,
    val botPaused: Boolean = false,
    val theme: String = "gold",
    val mt5Linked: Boolean = true,
    val mt5VerificationStatus: Mt5VerificationStatus = Mt5VerificationStatus.VERIFIED,
    val mt5Broker: String = "Exness (Raw Spread)",
    val verificationDocRef: String = "STMT-2026-XAU-981",
    val subscriptionPlan: String = "Monthly VIP Scalper"
)

data class TradingAnalytics(
    val totalTrades: Int = 24,
    val winRate: Double = 91.6,
    val averageProfit: Double = 54.20,
    val maxDrawdown: Double = 1.42,
    val totalProfit: Double = 1300.80,
    val winningTrades: Int = 22,
    val losingTrades: Int = 2
)

enum class NewsImpact {
    HIGH,
    MEDIUM,
    LOW
}

enum class SentimentType {
    BULLISH,
    BEARISH,
    NEUTRAL
}

data class NewsEvent(
    val id: String,
    val headline: String,
    val currency: String = "USD",
    val impact: NewsImpact,
    val sentiment: SentimentType,
    val sentimentScore: Double, // e.g. -0.68
    val scheduledTime: String,
    val minutesUntil: Int,
    val forecast: String = "",
    val previous: String = ""
)

data class NewsFilterConfig(
    val isEnabled: Boolean = true,
    val bufferMinutes: Int = 30,
    val pauseOnHighImpact: Boolean = true,
    val autoResume: Boolean = true
)

data class VoiceConfig(
    val enabled: Boolean = true,
    val isFemale: Boolean = true,
    val language: String = "English" // "English", "Swahili", "Luganda"
)

data class CandleStick(
    val timestamp: Long,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val ema20: Float,
    val ema50: Float
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceUgx: Long,
    val priceFormatted: String,
    val durationText: String,
    val description: String,
    val isPopular: Boolean = false,
    val features: List<String>
)
