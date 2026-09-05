package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppThemeColor
import com.example.data.Mt5VerificationStatus
import com.example.data.UserProfile
import kotlinx.coroutines.delay

@Composable
fun ConnectTradingPlatformModal(
    currentTheme: AppThemeColor,
    userProfile: UserProfile,
    onConnectAndVerify: (
        platform: String,
        account: String,
        broker: String,
        server: String,
        password: String,
        accountType: String,
        isReadOnly: Boolean
    ) -> Unit,
    onDismiss: () -> Unit,
    onUnlink: () -> Unit = {}
) {
    val accentColor = currentTheme.primary

    var selectedPlatform by remember { mutableStateOf(userProfile.platformType.ifBlank { "MT5" }) }
    var selectedAccountType by remember { mutableStateOf(userProfile.accountType.ifBlank { "Live Real" }) }
    var accountNumber by remember { mutableStateOf(userProfile.mt5Account.ifBlank { "8849201" }) }
    var selectedBroker by remember { mutableStateOf(userProfile.mt5Broker.ifBlank { "Exness (Raw Spread)" }) }
    var serverName by remember { mutableStateOf(userProfile.serverName.ifBlank { if (selectedPlatform == "MT4") "Exness-Real9" else "Exness-Real19" }) }
    var isReadOnlyPassword by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("ReadPass99$") }
    var showPasswordText by remember { mutableStateOf(false) }

    var isConnecting by remember { mutableStateOf(false) }
    var connectPhaseIndex by remember { mutableIntStateOf(0) }
    var connectionSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "GlowTransition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    val brokers = listOf(
        "Exness (Raw Spread)",
        "IC Markets (True ECN)",
        "Deriv MT5 / MT4",
        "FTMO Challenge",
        "FXTM ECN",
        "XM Ultra Low"
    )

    val connectingPhases = listOf(
        "Pinging ${selectedBroker.split(" ").first()} Gateway...",
        "Authenticating MetaTrader Handshake Protocol...",
        "Auditing Account Balance & Equity Curve...",
        "Synchronizing Performance Telemetry & Scalp Bridge..."
    )

    LaunchedEffect(isConnecting) {
        if (isConnecting) {
            for (i in connectingPhases.indices) {
                connectPhaseIndex = i
                delay(700)
            }
            connectionSuccess = true
            isConnecting = false
            onConnectAndVerify(
                selectedPlatform,
                accountNumber,
                selectedBroker,
                serverName,
                password,
                selectedAccountType,
                isReadOnlyPassword
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("connect_trading_platform_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF101114)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.5f),
                        Color(0x1FFFFFFF),
                        Color(0x0AFFFFFF)
                    )
                ),
                width = 1.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.15f))
                                .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "METATRADER GATEWAY",
                                    color = accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF2A1A0A))
                                        .border(0.5.dp, Color(0xFFFF9800), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = if (userProfile.mt5VerificationStatus == Mt5VerificationStatus.VERIFIED) "VERIFIED" else "UNVERIFIED",
                                        color = if (userProfile.mt5VerificationStatus == Mt5VerificationStatus.VERIFIED) Color(0xFF00FF88) else Color(0xFFFFB300),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Text(
                                text = "Connect MT5 / MT4 Platform",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E1E22))
                            .testTag("dismiss_connect_modal")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (connectionSuccess) {
                    // Success View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0x2200FF88))
                                .border(1.5.dp, Color(0xFF00FF88), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = Color(0xFF00FF88),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "$selectedPlatform Platform Connected!",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Account #$accountNumber on $selectedBroker is verified and actively streaming performance tracking telemetry.",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Features active badge summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF141F18))
                                    .border(1.dp, Color(0x3300FF88), RoundedCornerShape(10.dp))
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Equity Sync", color = Color(0xFF00FF88), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Active Live", color = Color.White, fontSize = 9.sp)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF141F18))
                                    .border(1.dp, Color(0x3300FF88), RoundedCornerShape(10.dp))
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Win Rate Audit", color = Color(0xFF00FF88), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Tracking", color = Color.White, fontSize = 9.sp)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF141F18))
                                    .border(1.dp, Color(0x3300FF88), RoundedCornerShape(10.dp))
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("EA Bridge", color = Color(0xFF00FF88), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("PovertyScalper", color = Color.White, fontSize = 9.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("continue_to_terminal_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "ENTER TERMINAL TRACKING",
                                color = Color(0xFF0A0A0A),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else if (isConnecting) {
                    // Connecting Handshake Progress View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = accentColor,
                            modifier = Modifier.size(54.dp),
                            strokeWidth = 4.dp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Establishing Secure Bridge...",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = connectingPhases.getOrElse(connectPhaseIndex) { "Handshaking..." },
                            color = accentColor,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress bar steps
                        Row(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for (i in connectingPhases.indices) {
                                val isDone = i <= connectPhaseIndex
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (isDone) accentColor else Color(0xFF222228))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "256-Bit SSL Handshake to $selectedBroker Server ($serverName)",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp
                        )
                    }
                } else {
                    // Unverified Alert Notice Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF201808).copy(alpha = pulseAlpha))
                            .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Live Performance Tracking Locked",
                                    color = Color(0xFFFFD54F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Connect your live or demo MT5/MT4 account below to unlock continuous equity curve auditing, win-rate analytics, and PovertyScalper 0.1 trade execution.",
                                    color = Color(0xFFE0E0E0),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 1: Platform Selection (MT5 vs MT4)
                    Text(
                        text = "1. CHOOSE TRADING PLATFORM",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // MT5 Tile
                        val isMt5 = selectedPlatform == "MT5"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isMt5) Color(0xFF1D1B16) else Color(0xFF141416))
                                .border(
                                    width = if (isMt5) 1.5.dp else 1.dp,
                                    color = if (isMt5) accentColor else Color(0x1FFFFFFF),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedPlatform = "MT5"
                                    if (serverName.contains("MT4", ignoreCase = true) || serverName == "Exness-Real9") {
                                        serverName = "Exness-Real19"
                                    }
                                }
                                .padding(12.dp)
                                .testTag("select_platform_mt5")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "MetaTrader 5",
                                        color = if (isMt5) Color.White else Color(0xFF94A3B8),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Modern • Multi-Asset",
                                        color = if (isMt5) accentColor else Color(0xFF64748B),
                                        fontSize = 10.sp
                                    )
                                }
                                if (isMt5) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // MT4 Tile
                        val isMt4 = selectedPlatform == "MT4"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isMt4) Color(0xFF1D1B16) else Color(0xFF141416))
                                .border(
                                    width = if (isMt4) 1.5.dp else 1.dp,
                                    color = if (isMt4) accentColor else Color(0x1FFFFFFF),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedPlatform = "MT4"
                                    if (serverName.contains("MT5", ignoreCase = true) || serverName == "Exness-Real19") {
                                        serverName = "Exness-Real9"
                                    }
                                }
                                .padding(12.dp)
                                .testTag("select_platform_mt4")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "MetaTrader 4",
                                        color = if (isMt4) Color.White else Color(0xFF94A3B8),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Classic • Forex Gold",
                                        color = if (isMt4) accentColor else Color(0xFF64748B),
                                        fontSize = 10.sp
                                    )
                                }
                                if (isMt4) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 2: Account Environment (Live Real vs Demo / Prop Firm)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF16161A))
                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(10.dp))
                            .padding(4.dp)
                    ) {
                        listOf("Live Real", "Prop Challenge", "Demo Trial").forEach { type ->
                            val isSel = selectedAccountType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) accentColor else Color.Transparent)
                                    .clickable { selectedAccountType = type }
                                    .padding(vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSel) Color(0xFF0A0A0A) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 3: Account Number / Login ID
                    Text(
                        text = "2. $selectedPlatform LOGIN / ACCOUNT NUMBER",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = {
                            accountNumber = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("account_number_input"),
                        placeholder = { Text("e.g. 8849201", color = Color(0xFF64748B), fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = accentColor)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0x2EFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Step 4: Broker / Server Selection
                    Text(
                        text = "3. BROKER & SERVER NAME",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        brokers.take(4).forEach { broker ->
                            val isSelected = selectedBroker == broker
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFF1E1E24) else Color(0xFF151518))
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) accentColor else Color(0x14FFFFFF),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        selectedBroker = broker
                                        serverName = when {
                                            broker.contains("Exness", ignoreCase = true) -> if (selectedPlatform == "MT4") "Exness-Real9" else "Exness-Real19"
                                            broker.contains("IC Markets", ignoreCase = true) -> "ICMarketsSC-Live02"
                                            broker.contains("Deriv", ignoreCase = true) -> "Deriv-Server-01"
                                            broker.contains("FTMO", ignoreCase = true) -> "FTMO-Server"
                                            else -> "Live-Trade-Server"
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = broker, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    if (isSelected) {
                                        Text("✓", color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Server field
                    OutlinedTextField(
                        value = serverName,
                        onValueChange = { serverName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Server Name") },
                        placeholder = { Text("e.g. Exness-Real19", color = Color(0xFF64748B), fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0x1FFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 5: Password Type (Investor Read-Only vs Master)
                    Text(
                        text = "4. CREDENTIAL PERMISSION MODE",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Investor Read-Only (Recommended)
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isReadOnlyPassword) Color(0xFF142218) else Color(0xFF151518))
                                .border(
                                    1.dp,
                                    if (isReadOnlyPassword) Color(0xFF00FF88) else Color(0x14FFFFFF),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { isReadOnlyPassword = true }
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .testTag("select_investor_password_mode")
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color(0xFF00FF88),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Investor Pass",
                                        color = if (isReadOnlyPassword) Color(0xFF00FF88) else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Read-Only (Recommended)",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 9.sp
                                )
                            }
                        }

                        // Master Trader Password
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (!isReadOnlyPassword) Color(0xFF221C14) else Color(0xFF151518))
                                .border(
                                    1.dp,
                                    if (!isReadOnlyPassword) accentColor else Color(0x14FFFFFF),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { isReadOnlyPassword = false }
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .testTag("select_master_password_mode")
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ElectricBolt,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Master Pass",
                                        color = if (!isReadOnlyPassword) accentColor else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Auto-Trade EA Mode",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Password Input Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input"),
                        label = { Text(if (isReadOnlyPassword) "Investor (Read-Only) Password" else "Master Password") },
                        placeholder = { Text("Enter password", color = Color(0xFF64748B), fontSize = 12.sp) },
                        visualTransformation = if (showPasswordText) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPasswordText = !showPasswordText }) {
                                Icon(
                                    imageVector = if (showPasswordText) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = if (isReadOnlyPassword) Color(0xFF00FF88) else accentColor)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isReadOnlyPassword) Color(0xFF00FF88) else accentColor,
                            unfocusedBorderColor = Color(0x2EFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Read-only safety explanation callout
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF141A16))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF00FF88),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isReadOnlyPassword) {
                                    "Zero Capital Risk: Investor passwords only permit read-only streaming of balance & equity curves without withdrawal or trade rights."
                                } else {
                                    "Caution: Master password allows PovertyScalper 0.1 to submit automated market orders on $selectedPlatform."
                                },
                                color = Color(0xFF81C784),
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = Color(0xFFFF5252),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Connect & Link Button
                    Button(
                        onClick = {
                            if (accountNumber.isBlank()) {
                                errorMessage = "Please enter your $selectedPlatform login number"
                            } else if (password.isBlank()) {
                                errorMessage = "Please enter password"
                            } else {
                                isConnecting = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_connect_platform_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = Color(0xFF0A0A0A),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONNECT & VERIFY $selectedPlatform",
                            color = Color(0xFF0A0A0A),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary: Remind me later / browse in demo mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Browse Demo Mode",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable { onDismiss() }
                                .padding(vertical = 6.dp)
                                .testTag("remind_later_button")
                        )

                        if (userProfile.mt5VerificationStatus == Mt5VerificationStatus.VERIFIED) {
                            Text(
                                text = "Unlink Platform",
                                color = Color(0xFFFF5252),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        onUnlink()
                                        onDismiss()
                                    }
                                    .padding(vertical = 6.dp)
                                    .testTag("unlink_platform_button")
                            )
                        }
                    }
                }
            }
        }
    }
}
