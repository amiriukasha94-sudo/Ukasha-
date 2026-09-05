package com.example.ui.components

import androidx.compose.runtime.Composable
import com.example.data.AppThemeColor
import com.example.data.UserProfile

@Composable
fun Mt5VerificationDialog(
    currentTheme: AppThemeColor,
    userProfile: UserProfile,
    onLinkAndVerify: (mt5Account: String, broker: String, investorPassword: String, statementRef: String) -> Unit,
    onDismiss: () -> Unit,
    onUnlink: () -> Unit = {}
) {
    ConnectTradingPlatformModal(
        currentTheme = currentTheme,
        userProfile = userProfile,
        onConnectAndVerify = { platform, account, broker, server, password, accountType, isReadOnly ->
            onLinkAndVerify(account, broker, password, "LINKED-$platform-$account")
        },
        onDismiss = onDismiss,
        onUnlink = onUnlink
    )
}
