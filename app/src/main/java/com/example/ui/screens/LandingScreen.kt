package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppThemeColor
import com.example.data.BotState
import com.example.ui.components.PovertyScalperBotDisplay
import com.example.ui.components.TeslaBotAvatar

@Composable
fun LandingScreen(
    currentTheme: AppThemeColor,
    botState: BotState,
    onNavigateToAuth: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onOpenPayment: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val accentColor = currentTheme.primary

    Box(modifier = modifier.fillMaxSize()) {
        // AI Robot Background Image with dark overlay
        Image(
            painter = painterResource(id = R.drawable.img_robot_bg),
            contentDescription = "PovertyScalper 0.1 Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xF50A0A0A),
                            Color(0xFC0A0A0A),
                            Color(0xFF0A0A0A)
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Bar Header - Geometric Balance Style
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(accentColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PS",
                                color = Color(0xFF0A0A0A),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CleanGold",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.2).sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "v1.20",
                                    color = accentColor.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "POVERTYSCALPER 0.1",
                                color = accentColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        // WhatsApp icon
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF142018))
                                .border(1.dp, Color(0x3300E676), CircleShape)
                                .clickable {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/256765014053?text=Hello%20CleanGold%20PovertyScalper%20Team")
                                    )
                                    context.startActivity(intent)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💬", fontSize = 14.sp)
                        }

                        Button(
                            onClick = onNavigateToAuth,
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Client Login", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Hero Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
                        width = 1.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(accentColor.copy(alpha = 0.15f))
                                .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "POVERTYSCALPER 0.1 • PROP CHALLENGE ENGINE",
                                color = accentColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Stop blowing accounts.\n1% risk, News Filter,\nAccount Locked.",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Automated high-probability gold scalping engineered for funded prop accounts. Real-time news shield and locked 1% drawdown ceiling.",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large, prominent PovertyScalper 0.1 Bot Display
                        PovertyScalperBotDisplay(
                            botState = botState,
                            accentColor = accentColor,
                            showControls = false,
                            avatarHeight = 220.dp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onNavigateToDashboard,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Launch Terminal", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF0A0A0A), modifier = Modifier.size(16.dp))
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/256765014053?text=Hello%20CleanGold%20PovertyScalper%200.1%20team,%20I%20want%20to%20inquire%20about%20the%20EA.")
                                    )
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF25D366))
                            ) {
                                Text("WhatsApp +256", color = Color(0xFF25D366), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Feature Highlights Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeaturePill(Icons.Default.Security, "1% Max Risk", accentColor, Modifier.weight(1f))
                    FeaturePill(Icons.Default.Newspaper, "News Shield", accentColor, Modifier.weight(1f))
                    FeaturePill(Icons.Default.Lock, "MT5 Verified", accentColor, Modifier.weight(1f))
                    FeaturePill(Icons.Default.Verified, "Prop Passed", accentColor, Modifier.weight(1f))
                }
            }

            // Comparison Table
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
                        width = 1.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "ALGORITHM BENCHMARK",
                            color = accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Black Nova vs PovertyScalper 0.1",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E1E24), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("METRIC", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                            Text("BLACK NOVA", color = Color(0xFFFF7A7A), fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("POVERTYSCALPER", color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        ComparisonRow("Lot Sizing", "4.0 lots fixed", "0.05 lot dynamic (1%)", accentColor)
                        ComparisonRow("News Filter", "None (Blows on CPI)", "30m Auto-Shield", accentColor)
                        ComparisonRow("Max Drawdown", "42.8% (Violated)", "1.42% (Protected)", accentColor)
                        ComparisonRow("Account Security", "Unprotected", "Locked to MT5 #", accentColor)
                        ComparisonRow("Win Rate", "48% (High Vol)", "91.6% (Verified)", accentColor)
                        ComparisonRow("Outcome", "BLOWS ACCOUNT", "SURVIVES & PASSES", accentColor)
                    }
                }
            }

            // Myfxbook Embed / Proof of Performance
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
                        width = 1.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("MYFXBOOK VERIFIED AUDIT", color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                                Text("PovertyScalper 0.1 Track Record", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x2200FF88))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("100% REAL TICK DATA", color = Color(0xFF00FF88), fontSize = 9.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatItem("Gain", "+124.5%", Color(0xFF00FF88))
                            StatItem("Max DD", "1.42%", accentColor)
                            StatItem("Win Rate", "91.6%", Color(0xFF00D4FF))
                            StatItem("Trades", "248", Color.White)
                        }
                    }
                }
            }

            // Pricing Section in UGX with Instant Payment
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "INSTANT UGX LICENSES",
                            color = accentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Subscription Plans (Uganda)",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "MTN / Airtel MoMo",
                        color = Color(0xFF00FF88),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Tier 1: Free Demo
                    PricingTierCard(
                        title = "Free Demo",
                        price = "UGX 0",
                        subtitle = "7 Days Free Trial • MT5 Demo",
                        features = listOf("1 Demo MT5 Account", "News Filter Active", "PovertyScalper 0.1 Engine"),
                        accent = Color.Gray,
                        buttonText = "Start Free 7-Day Demo",
                        onSelect = onNavigateToDashboard
                    )

                    // Tier 2: Monthly (Highlighted)
                    PricingTierCard(
                        title = "Monthly Scalper Pro",
                        price = "UGX 110,000",
                        subtitle = "30 Days Access (~$30) • Best for Prop Challenges",
                        features = listOf(
                            "1 Live MT5 Account",
                            "Instant MTN & Airtel MoMo Payment",
                            "News Sentiment Auto-Shield",
                            "1% Risk Limiter",
                            "Voice Alerts (English, Swahili, Luganda)"
                        ),
                        accent = accentColor,
                        isFeatured = true,
                        buttonText = "Instant Pay UGX 110,000",
                        onSelect = onOpenPayment
                    )

                    // Tier 3: Lifetime
                    PricingTierCard(
                        title = "Lifetime VIP Scalper",
                        price = "UGX 380,000",
                        subtitle = "Permanent License (~$100) • Zero Monthly Fees",
                        features = listOf(
                            "Unlimited Live MT5 Accounts",
                            "Instant Mobile Money Activation",
                            "Dedicated WhatsApp Concierge (+256)",
                            "Private High-Win Presets",
                            "Lifetime PovertyScalper Upgrades"
                        ),
                        accent = Color(0xFF00D4FF),
                        buttonText = "Instant Pay UGX 380,000",
                        onSelect = onOpenPayment
                    )
                }
            }

            // WhatsApp Direct Contact Banner (+256765014053)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 90.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1D13)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF25D366)),
                        width = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF25D366)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Direct WhatsApp & MoMo Payment", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("0765 014053 (+256 765 014 053)", color = Color(0xFF81C784), fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/256765014053?text=Hello%20CleanGold%20PovertyScalper%20Support"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Chat", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF18181E))
            .border(1.dp, Color(0x10FFFFFF), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    bad: String,
    good: String,
    accent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.White, fontSize = 11.sp, modifier = Modifier.weight(1.2f))
        Text(text = bad, color = Color(0xFFFF7A7A), fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        Text(text = good, color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
    }
}

@Composable
private fun StatItem(title: String, value: String, color: Color) {
    Column {
        Text(text = title, color = Color.Gray, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PricingTierCard(
    title: String,
    price: String,
    subtitle: String,
    features: List<String>,
    accent: Color,
    buttonText: String,
    isFeatured: Boolean = false,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFeatured) Color(0xFF1A1A22) else Color(0xFF141416)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isFeatured) accent else Color(0x14FFFFFF)
            ),
            width = if (isFeatured) 1.5.dp else 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(text = subtitle, color = Color.Gray, fontSize = 11.sp)
                }
                Text(text = price, color = accent, fontSize = 17.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            features.forEach { feat ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = accent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = feat, color = Color(0xFFD1D5DB), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFeatured) accent else Color(0xFF24242E)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = buttonText,
                    color = if (isFeatured) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
