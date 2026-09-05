package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppThemeColor
import com.example.data.SubscriptionPlan

@Composable
fun UgxPaymentDialog(
    currentTheme: AppThemeColor,
    plans: List<SubscriptionPlan>,
    onProcessPayment: (planId: String, phoneNumber: String, provider: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = currentTheme.primary

    var selectedPlanId by remember { mutableStateOf("monthly") }
    var selectedProvider by remember { mutableStateOf("MTN MoMo") } // "MTN MoMo" or "Airtel Money"
    var phoneNumber by remember { mutableStateOf("0765014053") }
    var isProcessing by remember { mutableStateOf(false) }
    var paymentSuccessMessage by remember { mutableStateOf<String?>(null) }

    val selectedPlan = plans.find { it.id == selectedPlanId } ?: plans.first()

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
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(accentColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "INSTANT UGX CHECKOUT",
                                color = accentColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "PovertyScalper 0.1 License",
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

                if (paymentSuccessMessage != null) {
                    // Success View
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
                            text = "Payment Verified Successfully!",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = paymentSuccessMessage ?: "",
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
                            Text("Open PovertyScalper Terminal", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Plan Selection Cards (in UGX)
                    Text(
                        text = "SELECT PLAN (UGX PRICING)",
                        color = Color(0xFF64748B),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        plans.forEach { plan ->
                            val isSelected = selectedPlanId == plan.id
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Color(0xFF1C1C20) else Color(0xFF161618))
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) accentColor else Color(0x14FFFFFF),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedPlanId = plan.id }
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = plan.name,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (plan.isPopular) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(accentColor)
                                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                                ) {
                                                    Text("HOT", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                                }
                                            }
                                        }
                                        Text(
                                            text = plan.durationText,
                                            color = Color(0xFF94A3B8),
                                            fontSize = 11.sp
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = plan.priceFormatted,
                                            color = if (isSelected) accentColor else Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = if (plan.priceUgx == 0L) "Free Demo" else "Instant Pay",
                                            color = Color(0xFF00FF88),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedPlan.priceUgx > 0L) {
                        // Mobile Money Network Selector
                        Text(
                            text = "INSTANT MOBILE MONEY NETWORK",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // MTN MoMo
                            val isMtn = selectedProvider == "MTN MoMo"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isMtn) Color(0xFF262414) else Color(0xFF18181A))
                                    .border(
                                        width = 1.dp,
                                        color = if (isMtn) Color(0xFFFFCC00) else Color(0x14FFFFFF),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedProvider = "MTN MoMo" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MTN MoMo (*165#)",
                                    color = if (isMtn) Color(0xFFFFCC00) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Airtel Money
                            val isAirtel = selectedProvider == "Airtel Money"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isAirtel) Color(0xFF281414) else Color(0xFF18181A))
                                    .border(
                                        width = 1.dp,
                                        color = if (isAirtel) Color(0xFFFF3333) else Color(0x14FFFFFF),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedProvider = "Airtel Money" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Airtel Money (*185#)",
                                    color = if (isAirtel) Color(0xFFFF5252) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Official Payment & WhatsApp Number Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1C12)),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFCC00).copy(alpha = 0.6f)),
                                width = 1.dp
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "OFFICIAL PAYMENT & WHATSAPP NUMBER",
                                            color = Color(0xFFFFCC00),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.6.sp
                                        )
                                        Text(
                                            text = "0765 014053",
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = "Recipient: CleanGold / PovertyScalper VIP Desk",
                                            color = Color(0xFFCBD5E1),
                                            fontSize = 10.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFFFCC00))
                                            .clickable {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("Payment Number", "0765014053"))
                                                Toast.makeText(context, "Copied 0765 014053 to clipboard", Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.Black, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("COPY", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Direct USSD: MTN (*165*1*0765014053*Amount#) | Airtel (*185*1*0765014053*Amount#)",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Phone Number Input
                        Text(
                            text = "YOUR PHONE NUMBER (+256)",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. 0765014053", color = Color(0xFF64748B), fontSize = 13.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = Color(0x1AFFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Action Button
                    Button(
                        onClick = {
                            if (selectedPlan.priceUgx == 0L) {
                                paymentSuccessMessage = "Free Demo activated for 7 days! MT5 link ready."
                            } else {
                                isProcessing = true
                                onProcessPayment(selectedPlan.id, phoneNumber, selectedProvider) { success, msg ->
                                    isProcessing = false
                                    if (success) {
                                        paymentSuccessMessage = msg
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isProcessing
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(color = Color(0xFF0A0A0A), modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Awaiting USSD Authorization...", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        } else {
                            Text(
                                text = if (selectedPlan.priceUgx == 0L) "ACTIVATE FREE DEMO" else "PAY ${selectedPlan.priceFormatted} NOW",
                                color = Color(0xFF0A0A0A),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Direct WhatsApp Concierge Button (+256765014053)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF142018))
                            .border(1.dp, Color(0x3300E676), RoundedCornerShape(14.dp))
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/256765014053?text=Hello%20CleanGold%20PovertyScalper%20Team,%20I%20want%20to%20subscribe%20to%20the%20${selectedPlan.name}%20plan%20(${selectedPlan.priceFormatted}).")
                                )
                                context.startActivity(intent)
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💬", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WhatsApp Support & MoMo VIP: +256 765 014 053",
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
}
