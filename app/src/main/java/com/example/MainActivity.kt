package com.example

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppThemeColor
import com.example.data.BotState
import com.example.data.TradingRepository
import com.example.service.VoiceManager
import com.example.ui.components.DraggableFloatingBubble
import com.example.ui.components.Mt5VerificationDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.TeslaBotAvatar
import com.example.ui.components.UgxPaymentDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.theme.CleanGoldTheme

enum class AppScreen {
    LANDING,
    AUTH,
    DASHBOARD,
    ADMIN
}

class MainActivity : ComponentActivity() {
    private lateinit var voiceManager: VoiceManager
    private lateinit var repository: TradingRepository
    private var isPipMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        voiceManager = VoiceManager(this)
        repository = TradingRepository(this, voiceManager)

        setContent {
            val currentTheme by repository.theme.collectAsState()
            val userProfile by repository.currentUser.collectAsState()
            val botState by repository.botState.collectAsState()
            val isBotSpeaking by repository.isBotSpeaking.collectAsState()
            val voiceConfig by repository.voiceConfig.collectAsState()
            val todayProfit by repository.todayProfit.collectAsState()
            val currentGoldPrice by repository.currentGoldPrice.collectAsState()
            val candles by repository.candles.collectAsState()
            val trades by repository.trades.collectAsState()
            val openTrades by repository.openTrades.collectAsState()
            val analytics by repository.analytics.collectAsState()
            val newsEvents by repository.newsEvents.collectAsState()
            val newsFilterConfig by repository.newsFilterConfig.collectAsState()
            val allUsers by repository.allUsers.collectAsState()
            val copyList by repository.copyList.collectAsState()

            var currentScreen by remember { mutableStateOf(AppScreen.LANDING) }
            var showSettingsDialog by remember { mutableStateOf(false) }
            var showUgxPaymentDialog by remember { mutableStateOf(false) }
            var showMt5VerificationDialog by remember { mutableStateOf(false) }

            CleanGoldTheme(themeColor = currentTheme) {
                if (isPipMode) {
                    // Picture-in-Picture Compact Layout
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0A0A0A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TeslaBotAvatar(
                                botState = botState,
                                accentColor = currentTheme.primary,
                                isSpeaking = isBotSpeaking,
                                size = 68.dp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "P&L: +$${String.format("%.2f", todayProfit)}",
                                color = Color(0xFF00FF88),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = botState.title,
                                color = currentTheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // Normal Full Layout
                    Box(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = Color(0xFF0A0A0A),
                            bottomBar = {
                                if (currentScreen == AppScreen.DASHBOARD || currentScreen == AppScreen.ADMIN) {
                                    val isAdminUser = userProfile.email.equals("ukashaamiri865@gmail.com", ignoreCase = true)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .background(Color(0xFF0A0A0A))
                                            .border(
                                                width = 1.dp,
                                                color = Color(0x14FFFFFF),
                                                shape = androidx.compose.ui.graphics.RectangleShape
                                            ),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val isTerminal = currentScreen == AppScreen.DASHBOARD
                                        // Terminal Tab
                                        Column(
                                            modifier = Modifier.clickable { currentScreen = AppScreen.DASHBOARD },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(if (isTerminal) currentTheme.primary else Color.Transparent)
                                                    .border(
                                                        1.dp,
                                                        if (isTerminal) currentTheme.primary else Color(0x40FFFFFF),
                                                        RoundedCornerShape(3.dp)
                                                    )
                                            )
                                            Text(
                                                text = "Terminal",
                                                color = if (isTerminal) currentTheme.primary else Color(0xFF64748B),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        // Overview Tab
                                        Column(
                                            modifier = Modifier.clickable { currentScreen = AppScreen.LANDING },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(3.dp))
                                            )
                                            Text(
                                                text = "Overview",
                                                color = Color(0xFF64748B),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        // UGX MoMo Pay Tab
                                        Column(
                                            modifier = Modifier.clickable { showUgxPaymentDialog = true },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(Color(0xFF262414))
                                                    .border(1.dp, Color(0xFFFFCC00), RoundedCornerShape(3.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("U", color = Color(0xFFFFCC00), fontSize = 9.sp, fontWeight = FontWeight.Black)
                                            }
                                            Text(
                                                text = "UGX Pay",
                                                color = Color(0xFFFFCC00),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        // Admin / VIP Tab
                                        val isAdminActive = currentScreen == AppScreen.ADMIN
                                        Column(
                                            modifier = Modifier.clickable {
                                                if (isAdminUser) currentScreen = AppScreen.ADMIN else showSettingsDialog = true
                                            },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(if (isAdminActive) currentTheme.primary else Color.Transparent)
                                                    .border(
                                                        1.dp,
                                                        if (isAdminActive) currentTheme.primary else Color(0x40FFFFFF),
                                                        RoundedCornerShape(3.dp)
                                                    )
                                            )
                                            Text(
                                                text = if (isAdminUser) "Admin" else "VIP",
                                                color = if (isAdminActive) currentTheme.primary else Color(0xFF64748B),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        // Settings Tab
                                        Column(
                                            modifier = Modifier.clickable { showSettingsDialog = true },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(3.dp))
                                            )
                                            Text(
                                                text = "Settings",
                                                color = Color(0xFF64748B),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        ) { innerPadding ->
                            AnimatedContent(
                                targetState = currentScreen,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "ScreenTransition"
                            ) { screen ->
                                when (screen) {
                                    AppScreen.LANDING -> LandingScreen(
                                        currentTheme = currentTheme,
                                        botState = botState,
                                        onNavigateToAuth = { currentScreen = AppScreen.AUTH },
                                        onNavigateToDashboard = { currentScreen = AppScreen.DASHBOARD },
                                        onOpenPayment = { showUgxPaymentDialog = true },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                    AppScreen.AUTH -> AuthScreen(
                                        currentTheme = currentTheme,
                                        botState = botState,
                                        onLoginSuccess = { email, mt5 ->
                                            repository.loginUser(email, mt5)
                                            currentScreen = AppScreen.DASHBOARD
                                        },
                                        onBackToLanding = { currentScreen = AppScreen.LANDING },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                    AppScreen.DASHBOARD -> DashboardScreen(
                                        currentTheme = currentTheme,
                                        userProfile = userProfile,
                                        botState = botState,
                                        isBotSpeaking = isBotSpeaking,
                                        todayProfit = todayProfit,
                                        currentGoldPrice = currentGoldPrice,
                                        candles = candles,
                                        trades = trades,
                                        openTrades = openTrades,
                                        analytics = analytics,
                                        newsEvents = newsEvents,
                                        newsFilterConfig = newsFilterConfig,
                                        onStartBot = { repository.startBot() },
                                        onStopBot = { repository.stopBot() },
                                        onTogglePause = { repository.toggleBotPause() },
                                        onOpenSettings = { showSettingsDialog = true },
                                        onOpenPayment = { showUgxPaymentDialog = true },
                                        onOpenMt5Verification = { showMt5VerificationDialog = true },
                                        onToggleNewsFilter = { repository.toggleNewsFilter() },
                                        onNewsBufferChanged = { repository.updateNewsBuffer(it) },
                                        onNavigateToAdmin = { currentScreen = AppScreen.ADMIN },
                                        onLogout = {
                                            repository.logout()
                                            currentScreen = AppScreen.LANDING
                                        },
                                        onSelectBotState = { repository.setBotState(it) },
                                        onExecuteScalp = { type, entryPrice, sl, tp ->
                                            repository.executeManualScalpTrade(type, entryPrice, sl, tp)
                                        },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                    AppScreen.ADMIN -> AdminScreen(
                                        currentTheme = currentTheme,
                                        allUsers = allUsers,
                                        copyList = copyList,
                                        onGenerateLicense = { mt5, email ->
                                            repository.generateLicense(mt5, email)
                                        },
                                        onAddClientToCopyList = { mt5 ->
                                            repository.addClientToCopyList(mt5)
                                        },
                                        onBackToDashboard = { currentScreen = AppScreen.DASHBOARD },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
                            }
                        }

                        // Draggable Floating Bubble always on screen across pages
                        DraggableFloatingBubble(
                            botState = botState,
                            accentColor = currentTheme.primary,
                            isSpeaking = isBotSpeaking,
                            openTradesCount = openTrades.size,
                            todayProfit = todayProfit,
                            userProfile = userProfile,
                            onTogglePause = { repository.toggleBotPause() },
                            onNavigateToDashboard = { currentScreen = AppScreen.DASHBOARD },
                            onEnterPip = { enterPictureInPicture() }
                        )

                        // Settings Dialog modal
                        if (showSettingsDialog) {
                            SettingsDialog(
                                currentTheme = currentTheme,
                                voiceConfig = voiceConfig,
                                onThemeSelected = { repository.setTheme(it) },
                                onVoiceConfigChanged = { repository.updateVoiceConfig(it) },
                                onTestVoice = {
                                    voiceManager.speak(BotState.BUYING, voiceConfig)
                                },
                                onEnterPip = { enterPictureInPicture() },
                                onDismiss = { showSettingsDialog = false }
                            )
                        }

                        // UGX Instant Payment Dialog Modal
                        if (showUgxPaymentDialog) {
                            UgxPaymentDialog(
                                currentTheme = currentTheme,
                                plans = repository.subscriptionPlans,
                                onProcessPayment = { planId, phone, provider, onResult ->
                                    repository.processInstantPayment(planId, phone, provider, onResult)
                                },
                                onDismiss = { showUgxPaymentDialog = false }
                            )
                        }

                        // MT5 Verification Dialog Modal
                        if (showMt5VerificationDialog) {
                            Mt5VerificationDialog(
                                currentTheme = currentTheme,
                                userProfile = userProfile,
                                onLinkAndVerify = { account, broker, pass, stmt ->
                                    repository.linkAndVerifyMt5(account, broker, pass, stmt)
                                },
                                onDismiss = { showMt5VerificationDialog = false }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun enterPictureInPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(1, 1))
                .build()
            enterPictureInPictureMode(params)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isPipMode = isInPictureInPictureMode
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // Optional: auto-enter PiP when user leaves app
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceManager.shutdown()
    }
}

// Kept for Roborazzi screenshot test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "CleanGold - PovertyScalper 0.1 ($name)",
        color = Color(0xFFFFD700),
        modifier = modifier
    )
}
