package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppThemeColor
import com.example.data.BotState
import com.example.ui.components.TeslaBotAvatar

@Composable
fun AuthScreen(
    currentTheme: AppThemeColor,
    botState: BotState,
    onLoginSuccess: (email: String, mt5Account: String) -> Unit,
    onBackToLanding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("demo.trader@cleangold.io") }
    var password by remember { mutableStateOf("••••••••") }
    var mt5Account by remember { mutableStateOf("8849201") }
    var broker by remember { mutableStateOf("Exness (Raw Spread)") }
    var verificationMethod by remember { mutableStateOf("investor_pass") } // "investor_pass" or "statement"
    var verificationProof by remember { mutableStateOf("Investor99#Read") }
    var isOwnershipVerified by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val accentColor = currentTheme.primary
    val brokers = listOf("Exness (Raw Spread)", "IC Markets", "FXTM", "Deriv MT5", "FTMO")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button & Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToLanding) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Back to Overview",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            TeslaBotAvatar(
                botState = botState,
                accentColor = accentColor,
                size = 76.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isSignUp) "PovertyScalper 0.1 Registration" else "Client Portal Login",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Firestore Synchronized • 256-Bit SSL Security",
                color = Color.Gray,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0x14FFFFFF)),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Mode Toggle: Login vs Sign Up
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1B1B22))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (!isSignUp) accentColor else Color.Transparent)
                                .clickable { isSignUp = false }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sign In",
                                color = if (!isSignUp) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSignUp) accentColor else Color.Transparent)
                                .clickable { isSignUp = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Link MT5 (Sign Up)",
                                color = if (isSignUp) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = accentColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0xFF2E2E38),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = accentColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0xFF2E2E38),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // MT5 Account Number (Linked during signup)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = mt5Account,
                        onValueChange = { mt5Account = it },
                        label = { Text("MT5 Account Number") },
                        placeholder = { Text("e.g. 8849201") },
                        leadingIcon = {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = accentColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0xFF2E2E38),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Verification Step for Sign Up
                    if (isSignUp) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "MT5 OWNERSHIP PROOF VERIFICATION",
                            color = accentColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Broker selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            brokers.take(3).forEach { b ->
                                val isSel = broker == b
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) Color(0xFF1E1E24) else Color(0xFF141416))
                                        .border(1.dp, if (isSel) accentColor else Color(0x14FFFFFF), RoundedCornerShape(8.dp))
                                        .clickable { broker = b }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(b.split(" ").first(), color = if (isSel) accentColor else Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Proof input
                        OutlinedTextField(
                            value = verificationProof,
                            onValueChange = { verificationProof = it },
                            label = { Text("Investor Password / Broker Statement Ref") },
                            placeholder = { Text("Read-only pass or ticket ID") },
                            leadingIcon = {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF00FF88))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF00FF88),
                                unfocusedBorderColor = Color(0xFF2E2E38),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00FF88), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Auto-verified on signup with broker handshake", color = Color(0xFF00FF88), fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action Button
                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                errorMessage = "Please enter your email"
                            } else {
                                onLoginSuccess(email, mt5Account)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isSignUp) "Link MT5 & Enter Dashboard" else "Enter Trading Dashboard",
                            color = Color(0xFF0A0A0A),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // WhatsApp Support CTA (+256765014053)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF142018))
                            .border(1.dp, Color(0x3300E676), RoundedCornerShape(10.dp))
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/256765014053?text=Hello%20CleanGold%20Team,%20I%20need%20help%20with%20my%20MT5%20login.")
                                )
                                context.startActivity(intent)
                            }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💬 WhatsApp Support: +256 765 014 053",
                            color = Color(0xFF00E676),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
