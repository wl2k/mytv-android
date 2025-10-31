package top.yogiczy.mytv.ui.screens.leanback.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import top.yogiczy.mytv.ui.theme.LeanbackTheme

@Composable
fun LeanbackQrcode(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium,
            )
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(8.dp),
            painter = rememberQrCodePainter(
                text
            ),
            contentDescription = text,
        )
    }
}

@Composable
fun LeanbackQrcodeDialog(
    modifier: Modifier = Modifier,
    text: String,
    description: String? = null,
    showDialogProvider: () -> Boolean = { false },
    onDismissRequest: () -> Unit = {},
) {
    if (showDialogProvider()) {
        AlertDialog(
            modifier = modifier,
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = onDismissRequest,
            confirmButton = { description?.let { Text(text = description) } },
            text = {
                Column {
                    LeanbackQrcode(
                        text = text,
                        modifier = Modifier
                            .width(240.dp)
                            .height(240.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun LeanbackQrcodeDialogPreview() {
    LeanbackTheme {
        LeanbackQrcodeDialog(
            text = "http://localhost:10481",
            showDialogProvider = { true }
        )
    }
}