package com.jop.jetpack.firebase.ui.component

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jop.jetpack.firebase.utils.goToAppSetting

@Composable
fun PermissionDialog(title: String, message: String, onDismiss: () -> Unit){
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss,) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
            )

            Button(
                modifier = Modifier.padding(top = 16.dp).defaultMinSize(minHeight = 48.dp),
                onClick = {
                    onDismiss()
                    (context as Activity).goToAppSetting()
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Buka Pengaturan Aplikasi",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }
    }
}

@Composable
fun CustomAlertDialog(title: String, message: String, btnConfirmText: String = "Oke", btnCancelText: String = "Batal", btnConfirmAction: () -> Unit = {}, btnCancelAction: () -> Unit = {}, onDismiss: () -> Unit){
    Dialog(onDismissRequest = onDismiss,) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton (
                    modifier = Modifier.weight(1f).padding(top = 16.dp).defaultMinSize(minHeight = 48.dp),
                    onClick = {
                        onDismiss()
                        btnCancelAction()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = btnCancelText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }

                Button(
                    modifier = Modifier.weight(1f).padding(top = 16.dp).defaultMinSize(minHeight = 48.dp),
                    onClick = {
                        onDismiss()
                        btnConfirmAction()
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = btnConfirmText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }
            }
        }
    }
}