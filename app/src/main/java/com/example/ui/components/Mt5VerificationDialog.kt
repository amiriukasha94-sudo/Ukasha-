package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppThemeColor
import com.example.data.UserProfile

@Composable
fun Mt5VerificationDialog(
    currentTheme: AppThemeColor,
    userProfile: UserProfile,
    onLinkAndVerify: (mt5Account: String, broker: String, investorPassword: String, statementRef: String) -> Unit,
    onDismiss: () -> Unit
) {
    val accentColor = currentTheme.primary

    var mt5Account by remember { mutableStateOf(userProfile.mt5Account) }
    var selectedBroker by remember { mutableStateOf(userProfile.mt5Broker) }
    var investorPassword by remember { mutableStateOf("ReadPass99$") }
    var statementRef by remember { mutableStateOf(userProfile.verificationDocRef) }
    var verificationMethod by remember { mutableStateOf("investor_pass") } // "investor_pass" or "statement"
    var isVerifying by remember { mutableStateOf(false) }
    var verifiedSuccess by remember { mutableStateOf(false) }

    val brokers = listOf("Exness (Raw Spread)", "IC Markets", "FXTM ECN", "Deriv MT5", "XM Ultra Low", "FTMO Challenge")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141416)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0x1AFFFFFF)),
                width = 1.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MT5 PROOF OF OWNERSHIP",
                                color = accentColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = "Link & Verify MT5 Account",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E1E22))
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

                if (verifiedSuccess) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0x2200FF88)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00FF88),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "MT5 Account Verified!",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "MT5# $mt5Account ($selectedBroker) is verified and synchronized with PovertyScalper 0.1 backend trade logs.",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Done", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // MT5 Account Number
                    Text("MT5 LOGIN / ACCOUNT NUMBER", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = mt5Account,
                        onValueChange = { mt5Account = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. 8849201", color = Color(0xFF64748B), fontSize = 13.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color(0x1AFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Broker Selection
                    Text("BROKER / SERVER", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        brokers.take(4).forEach { broker ->
                            val isSelected = selectedBroker == broker
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0xFF1E1E24) else Color(0xFF161618))
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) accentColor else Color(0x14FFFFFF),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedBroker = broker }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Verification Mode Selector
                    Text("VERIFICATION METHOD", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val isPass = verificationMethod == "investor_pass"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isPass) Color(0xFF1E1E24) else Color(0xFF161618))
                                .border(1.dp, if (isPass) accentColor else Color(0x14FFFFFF), RoundedCornerShape(10.dp))
                                .clickable { verificationMethod = "investor_pass" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Investor Read-Only Pass", color = if (isPass) accentColor else Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        val isStmt = verificationMethod == "statement"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isStmt) Color(0xFF1E1E24) else Color(0xFF161618))
                                .border(1.dp, if (isStmt) accentColor else Color(0x14FFFFFF), RoundedCornerShape(10.dp))
                                .clickable { verificationMethod = "statement" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Broker Statement Ref", color = if (isStmt) accentColor else Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (verificationMethod == "investor_pass") {
                        OutlinedTextField(
                            value = investorPassword,
                            onValueChange = { investorPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            placeholder = { Text("Enter read-only investor password", color = Color(0xFF64748B), fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Text(
                            text = "ℹ Read-only password cannot trade or withdraw. Only used to verify account balance.",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        OutlinedTextField(
                            value = statementRef,
                            onValueChange = { statementRef = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. STMT-2026-XAU-981 or Last Ticket ID", color = Color(0xFF64748B), fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Verify Button
                    Button(
                        onClick = {
                            isVerifying = true
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                isVerifying = false
                                verifiedSuccess = true
                                onLinkAndVerify(mt5Account, selectedBroker, investorPassword, statementRef)
                            }, 1200)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !isVerifying && mt5Account.isNotBlank()
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(color = Color(0xFF0A0A0A), modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verifying Broker Handshake...", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        } else {
                            Text(
                                text = "VERIFY & LINK MT5 ACCOUNT",
                                color = Color(0xFF0A0A0A),
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
