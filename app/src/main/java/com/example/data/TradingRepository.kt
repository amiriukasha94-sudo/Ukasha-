package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.service.VoiceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class TradingRepository(
    private val context: Context,
    private val voiceManager: VoiceManager
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("cleangold_prefs", Context.MODE_PRIVATE)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    // --- State Flows ---
    private val _theme = MutableStateFlow(loadSavedTheme())
    val theme: StateFlow<AppThemeColor> = _theme.asStateFlow()

    private val _currentUser = MutableStateFlow(loadSavedUser())
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _botState = MutableStateFlow(BotState.ANALYZING)
    val botState: StateFlow<BotState> = _botState.asStateFlow()

    private val _isBotSpeaking = MutableStateFlow(false)
    val isBotSpeaking: StateFlow<Boolean> = _isBotSpeaking.asStateFlow()

    private val _voiceConfig = MutableStateFlow(loadSavedVoiceConfig())
    val voiceConfig: StateFlow<VoiceConfig> = _voiceConfig.asStateFlow()

    private val _trades = MutableStateFlow(generateInitialTrades())
    val trades: StateFlow<List<Trade>> = _trades.asStateFlow()

    private val _openTrades = MutableStateFlow(generateInitialOpenTrades())
    val openTrades: StateFlow<List<Trade>> = _openTrades.asStateFlow()

    private val _candles = MutableStateFlow(generateInitialCandles())
    val candles: StateFlow<List<CandleStick>> = _candles.asStateFlow()

    private val _currentGoldPrice = MutableStateFlow(2502.45f)
    val currentGoldPrice: StateFlow<Float> = _currentGoldPrice.asStateFlow()

    private val _todayProfit = MutableStateFlow(384.50)
    val todayProfit: StateFlow<Double> = _todayProfit.asStateFlow()

    // Real-Time Analytics
    private val _analytics = MutableStateFlow(calculateAnalytics(_trades.value, _currentUser.value.equity))
    val analytics: StateFlow<TradingAnalytics> = _analytics.asStateFlow()

    // News Sentiment Filter
    private val _newsEvents = MutableStateFlow(generateInitialNewsEvents())
    val newsEvents: StateFlow<List<NewsEvent>> = _newsEvents.asStateFlow()

    private val _newsFilterConfig = MutableStateFlow(NewsFilterConfig(isEnabled = true, bufferMinutes = 30))
    val newsFilterConfig: StateFlow<NewsFilterConfig> = _newsFilterConfig.asStateFlow()

    // UGX Subscription Plans
    val subscriptionPlans: List<SubscriptionPlan> = listOf(
        SubscriptionPlan(
            id = "demo",
            name = "Free Demo Trial",
            priceUgx = 0L,
            priceFormatted = "UGX 0",
            durationText = "7 Days Free",
            description = "Risk-free trial on MT5 demo accounts with full news protection",
            features = listOf(
                "1 Demo MT5 Account",
                "PovertyScalper 0.1 Engine",
                "Automated News Shield",
                "Realtime VPS Telegram Feed"
            )
        ),
        SubscriptionPlan(
            id = "monthly",
            name = "Monthly Scalper Pro",
            priceUgx = 110000L,
            priceFormatted = "UGX 110,000",
            durationText = "30 Days Access",
            description = "Most popular for prop firm challenges and live personal accounts",
            isPopular = true,
            features = listOf(
                "1 Live MT5 Account",
                "Instant Mobile Money Activation",
                "News Sentiment Filter Integration",
                "1% Dynamic Risk Lock",
                "24/7 AI Voice Notifications"
            )
        ),
        SubscriptionPlan(
            id = "lifetime",
            name = "Lifetime VIP Scalper",
            priceUgx = 380000L,
            priceFormatted = "UGX 380,000",
            durationText = "One-Time Lifetime",
            description = "Permanent license with unlimited account migration & VIP broker setups",
            features = listOf(
                "Unlimited Live MT5 Accounts",
                "Zero Monthly Fees Forever",
                "Private WhatsApp Concierge (+256)",
                "Full Custom Indicator Presets",
                "Personal Copy Trading Access"
            )
        )
    )

    // Admin state
    private val _allUsers = MutableStateFlow(generateInitialUserList())
    val allUsers: StateFlow<List<UserProfile>> = _allUsers.asStateFlow()

    private val _copyList = MutableStateFlow(
        listOf("MT5# 8849102", "MT5# 8849311", "MT5# 8901244", "MT5# 8933019")
    )
    val copyList: StateFlow<List<String>> = _copyList.asStateFlow()

    private var simulationJob: Job? = null

    init {
        voiceManager.setSpeakingListener { speaking ->
            _isBotSpeaking.value = speaking
        }
        startMarketSimulation()
    }

    private fun loadSavedTheme(): AppThemeColor {
        val savedId = prefs.getString("user_theme", "gold") ?: "gold"
        return AppThemeColor.entries.find { it.id == savedId } ?: AppThemeColor.GOLD
    }

    private fun loadSavedVoiceConfig(): VoiceConfig {
        return VoiceConfig(
            enabled = prefs.getBoolean("voice_enabled", true),
            isFemale = prefs.getBoolean("voice_female", true),
            language = prefs.getString("voice_language", "English") ?: "English"
        )
    }

    private fun loadSavedUser(): UserProfile {
        val email = prefs.getString("user_email", "demo.trader@cleangold.io") ?: "demo.trader@cleangold.io"
        val mt5 = prefs.getString("user_mt5", "8849201") ?: "8849201"
        return UserProfile(
            email = email,
            mt5Account = mt5,
            expiryDate = "2026-10-18",
            expiryDaysRemaining = 12,
            expiryHoursRemaining = 4,
            isActive = true,
            balance = 10000.0,
            equity = 12450.80,
            botPaused = false,
            theme = prefs.getString("user_theme", "gold") ?: "gold",
            mt5Linked = true,
            mt5VerificationStatus = Mt5VerificationStatus.VERIFIED,
            mt5Broker = "Exness (Raw Spread)"
        )
    }

    fun setTheme(theme: AppThemeColor) {
        _theme.value = theme
        prefs.edit().putString("user_theme", theme.id).apply()
        _currentUser.update { it.copy(theme = theme.id) }
    }

    fun updateVoiceConfig(config: VoiceConfig) {
        _voiceConfig.value = config
        prefs.edit()
            .putBoolean("voice_enabled", config.enabled)
            .putBoolean("voice_female", config.isFemale)
            .putString("voice_language", config.language)
            .apply()
    }

    fun startBot() {
        _currentUser.update { it.copy(botPaused = false) }
        _botState.value = BotState.ANALYZING
        voiceManager.speak(BotState.ANALYZING, _voiceConfig.value)
    }

    fun stopBot() {
        _currentUser.update { it.copy(botPaused = true) }
        _botState.value = BotState.SLEEPING
        voiceManager.speak(BotState.SLEEPING, _voiceConfig.value)
    }

    fun toggleBotPause() {
        if (_currentUser.value.botPaused) {
            startBot()
        } else {
            stopBot()
        }
    }

    fun setBotState(state: BotState) {
        _botState.value = state
        voiceManager.speak(state, _voiceConfig.value)
    }

    // --- News Sentiment Filter Management ---
    fun toggleNewsFilter() {
        val updated = _newsFilterConfig.value.copy(isEnabled = !_newsFilterConfig.value.isEnabled)
        _newsFilterConfig.value = updated
        checkNewsFilterTrigger()
    }

    fun updateNewsBuffer(minutes: Int) {
        _newsFilterConfig.value = _newsFilterConfig.value.copy(bufferMinutes = minutes)
        checkNewsFilterTrigger()
    }

    private fun checkNewsFilterTrigger() {
        val config = _newsFilterConfig.value
        if (!config.isEnabled) return

        // Find upcoming high impact news within buffer
        val upcomingHighImpact = _newsEvents.value.firstOrNull {
            it.impact == NewsImpact.HIGH && it.minutesUntil <= config.bufferMinutes && it.minutesUntil >= -config.bufferMinutes
        }

        if (upcomingHighImpact != null) {
            _botState.value = BotState.NEWS_PAUSE
            voiceManager.speak(BotState.NEWS_PAUSE, _voiceConfig.value)
        }
    }

    // --- MT5 Account Linking & Verification ---
    fun linkAndVerifyMt5(
        mt5Account: String,
        broker: String,
        investorPassword: String,
        statementReference: String
    ) {
        val cleanAccount = mt5Account.trim().ifBlank { "8849" + Random.nextInt(100, 999) }
        val cleanBroker = broker.trim().ifBlank { "Exness (Raw Spread)" }
        val docRef = if (statementReference.isNotBlank()) statementReference.trim() else "STMT-2026-XAU-${Random.nextInt(100, 999)}"

        _currentUser.update {
            it.copy(
                mt5Account = cleanAccount,
                mt5Broker = cleanBroker,
                mt5Linked = true,
                mt5VerificationStatus = Mt5VerificationStatus.VERIFIED,
                verificationDocRef = docRef
            )
        }

        prefs.edit()
            .putString("user_mt5", cleanAccount)
            .putString("user_broker", cleanBroker)
            .apply()
    }

    // --- Chart Analyser Manual Scalp Trade Execution ---
    fun executeManualScalpTrade(
        type: String = "BUY",
        entryPrice: Double,
        sl: Double,
        tp: Double,
        lot: Double = 0.05
    ) {
        val profit = if (type == "BUY") Random.nextDouble(25.0, 85.0) else Random.nextDouble(20.0, 80.0)
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val newTrade = Trade(
            id = "XAU-" + Random.nextInt(1000, 9999),
            account = _currentUser.value.mt5Account,
            symbol = "XAUUSD",
            lot = lot,
            type = type,
            sl = sl,
            tp = tp,
            openPrice = entryPrice,
            closePrice = if (type == "BUY") entryPrice + 1.40 else entryPrice - 1.40,
            profit = profit,
            timestamp = dateFormat.format(Date()),
            isClosed = true
        )

        _trades.update { listOf(newTrade) + it.take(19) }
        _todayProfit.update { Math.round((it + profit) * 100) / 100.0 }
        _currentUser.update {
            it.copy(
                equity = Math.round((it.equity + profit) * 100) / 100.0
            )
        }
        _analytics.value = calculateAnalytics(_trades.value, _currentUser.value.equity)
        _botState.value = BotState.BUYING
        voiceManager.speak(BotState.BUYING, _voiceConfig.value)
    }

    // --- Instant Payment in UGX (MTN / Airtel Mobile Money) ---
    fun processInstantPayment(
        planId: String,
        phoneNumber: String,
        provider: String,
        onComplete: (success: Boolean, message: String) -> Unit
    ) {
        scope.launch {
            val selectedPlan = subscriptionPlans.find { it.id == planId } ?: subscriptionPlans[1]
            delay(1500) // Simulate instant network mobile money verification

            val isLifetime = planId == "lifetime"
            val addedDays = if (isLifetime) 3650 else 30

            _currentUser.update {
                it.copy(
                    isActive = true,
                    subscriptionPlan = selectedPlan.name,
                    expiryDaysRemaining = it.expiryDaysRemaining + addedDays,
                    expiryDate = if (isLifetime) "2036-12-31" else "2026-11-18"
                )
            }

            onComplete(
                true,
                "Payment of ${selectedPlan.priceFormatted} received via $provider ($phoneNumber). ${selectedPlan.name} is now ACTIVE on MT5# ${_currentUser.value.mt5Account}!"
            )
        }
    }

    fun loginUser(email: String, mt5Account: String) {
        val user = UserProfile(
            email = email,
            mt5Account = mt5Account.ifBlank { "8849" + Random.nextInt(100, 999) },
            expiryDate = "2026-10-18",
            expiryDaysRemaining = 12,
            expiryHoursRemaining = 4,
            isActive = true,
            balance = 10000.0,
            equity = 12450.80,
            botPaused = false,
            theme = _theme.value.id,
            mt5Linked = mt5Account.isNotBlank(),
            mt5VerificationStatus = if (mt5Account.isNotBlank()) Mt5VerificationStatus.VERIFIED else Mt5VerificationStatus.PENDING_VERIFICATION
        )
        _currentUser.value = user
        prefs.edit()
            .putString("user_email", email)
            .putString("user_mt5", user.mt5Account)
            .apply()

        _allUsers.update { current ->
            if (current.any { it.email.equals(email, ignoreCase = true) }) current
            else current + user
        }
    }

    fun logout() {
        loginUser("demo.trader@cleangold.io", "8849201")
    }

    // Admin panel actions
    fun generateLicense(mt5: String, clientEmail: String) {
        val newAccount = mt5.ifBlank { "88" + Random.nextInt(10000, 99999) }
        val newUser = UserProfile(
            email = clientEmail.ifBlank { "client_${Random.nextInt(100, 999)}@fxprop.com" },
            mt5Account = newAccount,
            expiryDate = "2026-12-31",
            expiryDaysRemaining = 30,
            expiryHoursRemaining = 0,
            isActive = true,
            balance = 25000.0,
            equity = 25000.0,
            botPaused = false,
            theme = _theme.value.id,
            mt5Linked = true,
            mt5VerificationStatus = Mt5VerificationStatus.VERIFIED
        )
        _allUsers.update { it + newUser }
    }

    fun addClientToCopyList(mt5: String) {
        val entry = "MT5# $mt5"
        if (!_copyList.value.contains(entry)) {
            _copyList.update { it + entry }
        }
    }

    private fun calculateAnalytics(tradeList: List<Trade>, currentEquity: Double): TradingAnalytics {
        val total = tradeList.size
        if (total == 0) return TradingAnalytics()

        val winning = tradeList.count { it.profit > 0 }
        val losing = tradeList.count { it.profit <= 0 }
        val winRate = (winning.toDouble() / total.toDouble()) * 100.0
        val sumProfit = tradeList.sumOf { it.profit }
        val avgProfit = sumProfit / total.toDouble()

        // Calculate max drawdown using peak equity
        val peak = maxOf(currentEquity, 12500.0)
        val maxDd = ((peak - (currentEquity - 180.0)) / peak) * 100.0

        return TradingAnalytics(
            totalTrades = total + 19, // total executed on account
            winRate = Math.round(winRate * 10) / 10.0,
            averageProfit = Math.round(avgProfit * 100) / 100.0,
            maxDrawdown = Math.round(maxDd.coerceIn(0.8, 3.5) * 100) / 100.0,
            totalProfit = Math.round(sumProfit * 100) / 100.0,
            winningTrades = winning + 17,
            losingTrades = losing + 2
        )
    }

    private fun startMarketSimulation() {
        simulationJob?.cancel()
        simulationJob = scope.launch {
            var tickCount = 0
            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

            while (isActive) {
                delay(3000)
                tickCount++

                // Update gold price with realistic slight movement
                val delta = (Random.nextFloat() - 0.48f) * 0.45f
                val newPrice = (_currentGoldPrice.value + delta).coerceIn(2490f, 2525f)
                _currentGoldPrice.value = (Math.round(newPrice * 100) / 100f)

                // Update candle stick list
                val currentList = _candles.value.toMutableList()
                if (currentList.isNotEmpty()) {
                    val last = currentList.last()
                    val updatedLast = last.copy(
                        high = maxOf(last.high, newPrice),
                        low = minOf(last.low, newPrice),
                        close = newPrice,
                        ema20 = (last.ema20 * 0.95f) + (newPrice * 0.05f),
                        ema50 = (last.ema50 * 0.98f) + (newPrice * 0.02f)
                    )
                    currentList[currentList.size - 1] = updatedLast
                    _candles.value = currentList
                }

                // Periodic news countdown tick & filter check
                if (tickCount % 4 == 0) {
                    _newsEvents.update { list ->
                        list.map { news ->
                            val updatedMinutes = if (news.minutesUntil > 1) news.minutesUntil - 1 else 60
                            news.copy(minutesUntil = updatedMinutes)
                        }
                    }
                    checkNewsFilterTrigger()
                }

                // If bot is paused by user, keep in sleeping state
                if (_currentUser.value.botPaused) continue

                // State cycle simulation every 15-25 seconds
                if (tickCount % 6 == 0) {
                    val nextState = when (Random.nextInt(5)) {
                        0 -> BotState.ANALYZING
                        1 -> BotState.BUYING
                        2 -> BotState.BLOCKED
                        3 -> BotState.NEWS_PAUSE
                        else -> BotState.ANALYZING
                    }
                    _botState.value = nextState
                    voiceManager.speak(nextState, _voiceConfig.value)

                    // If BUY state, simulate a scalping trade execution
                    if (nextState == BotState.BUYING) {
                        val profit = Random.nextDouble(18.0, 75.0)
                        val lot = 0.05
                        val entryPrice = (newPrice * 100).toInt() / 100.0
                        val sl = entryPrice - 4.50
                        val tp = entryPrice + 6.00

                        val newTrade = Trade(
                            id = "XAU-" + Random.nextInt(1000, 9999),
                            account = _currentUser.value.mt5Account,
                            symbol = "XAUUSD",
                            lot = lot,
                            type = "BUY",
                            sl = sl,
                            tp = tp,
                            openPrice = entryPrice,
                            closePrice = entryPrice + 1.20,
                            profit = profit,
                            timestamp = dateFormat.format(Date()),
                            isClosed = true
                        )

                        _trades.update { listOf(newTrade) + it.take(19) }
                        _todayProfit.update { Math.round((it + profit) * 100) / 100.0 }
                        _currentUser.update {
                            it.copy(
                                equity = Math.round((it.equity + profit) * 100) / 100.0
                            )
                        }
                        _analytics.value = calculateAnalytics(_trades.value, _currentUser.value.equity)
                    }
                }
            }
        }
    }

    private fun generateInitialNewsEvents(): List<NewsEvent> {
        return listOf(
            NewsEvent(
                id = "n1",
                headline = "US Core CPI Inflation (MoM & YoY)",
                currency = "USD",
                impact = NewsImpact.HIGH,
                sentiment = SentimentType.BEARISH,
                sentimentScore = -0.68,
                scheduledTime = "15:30 GMT",
                minutesUntil = 18,
                forecast = "0.3%",
                previous = "0.2%"
            ),
            NewsEvent(
                id = "n2",
                headline = "FOMC Powell Speaks at Economic Summit",
                currency = "USD",
                impact = NewsImpact.HIGH,
                sentiment = SentimentType.BULLISH,
                sentimentScore = 0.54,
                scheduledTime = "17:00 GMT",
                minutesUntil = 48,
                forecast = "Rate Hold",
                previous = "5.25%"
            ),
            NewsEvent(
                id = "n3",
                headline = "Central Bank Gold Reserves Monthly Report",
                currency = "XAU",
                impact = NewsImpact.MEDIUM,
                sentiment = SentimentType.BULLISH,
                sentimentScore = 0.82,
                scheduledTime = "19:00 GMT",
                minutesUntil = 110,
                forecast = "+28 Tons",
                previous = "+22 Tons"
            ),
            NewsEvent(
                id = "n4",
                headline = "US Initial Jobless Claims",
                currency = "USD",
                impact = NewsImpact.LOW,
                sentiment = SentimentType.NEUTRAL,
                sentimentScore = 0.05,
                scheduledTime = "21:30 GMT",
                minutesUntil = 240,
                forecast = "220K",
                previous = "218K"
            )
        )
    }

    private fun generateInitialTrades(): List<Trade> {
        return listOf(
            Trade("XAU-9821", "8849201", "XAUUSD", 0.05, "BUY", 2496.50, 2506.00, 2499.20, 2504.80, 56.00, "14:28:12", true),
            Trade("XAU-9818", "8849201", "XAUUSD", 0.05, "BUY", 2492.00, 2501.50, 2494.30, 2499.70, 54.00, "13:15:40", true),
            Trade("XAU-9804", "8849201", "XAUUSD", 0.05, "BUY", 2488.50, 2498.00, 2490.10, 2496.20, 61.00, "11:42:05", true),
            Trade("XAU-9791", "8849201", "XAUUSD", 0.05, "BUY", 2485.00, 2493.50, 2486.80, 2491.90, 51.00, "10:04:19", true),
            Trade("XAU-9775", "8849201", "XAUUSD", 0.05, "BUY", 2480.00, 2489.00, 2482.40, 2487.60, 52.00, "08:30:11", true)
        )
    }

    private fun generateInitialOpenTrades(): List<Trade> {
        return listOf(
            Trade("XAU-9840", "8849201", "XAUUSD", 0.05, "BUY", 2497.00, 2508.50, 2500.10, null, 23.50, "14:41:02", false),
            Trade("XAU-9842", "8849201", "XAUUSD", 0.05, "BUY", 2498.20, 2509.00, 2501.30, null, 11.20, "14:43:18", false)
        )
    }

    private fun generateInitialCandles(): List<CandleStick> {
        val list = mutableListOf<CandleStick>()
        var price = 2492f
        val now = System.currentTimeMillis()
        var ema20 = 2492f
        var ema50 = 2491f

        for (i in 30 downTo 0) {
            val open = price
            val delta = (Random.nextFloat() - 0.46f) * 2.2f
            val close = open + delta
            val high = maxOf(open, close) + Random.nextFloat() * 1.5f
            val low = minOf(open, close) - Random.nextFloat() * 1.5f
            ema20 = (ema20 * 0.90f) + (close * 0.10f)
            ema50 = (ema50 * 0.95f) + (close * 0.05f)

            list.add(
                CandleStick(
                    timestamp = now - (i * 5 * 60 * 1000L),
                    open = open,
                    high = high,
                    low = low,
                    close = close,
                    ema20 = ema20,
                    ema50 = ema50
                )
            )
            price = close
        }
        return list
    }

    private fun generateInitialUserList(): List<UserProfile> {
        return listOf(
            UserProfile("ukashaamiri865@gmail.com", "8849201", "2027-01-01", 118, 12, true, 50000.0, 58420.0, false, "gold"),
            UserProfile("alex.trader@propfirm.com", "8849102", "2026-09-30", 18, 6, true, 10000.0, 11340.50, false, "gold"),
            UserProfile("sarah.scalper@gmail.com", "8849311", "2026-10-15", 33, 14, true, 25000.0, 27950.0, false, "ice_blue"),
            UserProfile("john.fund@kapital.co", "8901244", "2026-11-20", 69, 2, true, 100000.0, 114200.0, false, "matrix_green"),
            UserProfile("david.gold@outlook.com", "8933019", "2026-09-24", 12, 4, true, 5000.0, 5620.10, true, "royal_purple")
        )
    }
}
