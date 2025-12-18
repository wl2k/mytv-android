package top.wl2k.mytv.ui.screens.leanback.panel.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import top.wl2k.mytv.ui.theme.LeanbackTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LeanbackPanelDateTime(
    modifier: Modifier = Modifier,
    timeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()),
    timestamp: Long = rememberTimestamp(),
) {
    Text(
        text = timeFormat.format(timestamp),
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun LeanbackPanelDateTimePreview() {
    LeanbackTheme {
        LeanbackPanelDateTime()
    }
}

@Composable
private fun rememberTimestamp(): Long {
    var timestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timestamp = System.currentTimeMillis()
        }
    }

    return timestamp
}