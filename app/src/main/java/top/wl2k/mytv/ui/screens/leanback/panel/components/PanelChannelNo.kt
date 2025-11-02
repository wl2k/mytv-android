package top.wl2k.mytv.ui.screens.leanback.panel.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import top.wl2k.mytv.ui.theme.LeanbackTheme

@Composable
fun LeanbackPanelChannelNo(
    modifier: Modifier = Modifier,
    fontSize: Int = 57,
    channelNoProvider: () -> String,
) {
    Text(
        modifier = modifier,
        text = channelNoProvider(),
        style = MaterialTheme.typography.displayLarge.copy(
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
        ),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Preview
@Composable
private fun LeanbackPanelChannelNoPreview() {
    LeanbackTheme {
        LeanbackPanelChannelNo(
            channelNoProvider = { "10" }
        )
    }
}