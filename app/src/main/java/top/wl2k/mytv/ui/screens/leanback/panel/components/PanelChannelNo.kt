package top.wl2k.mytv.ui.screens.leanback.panel.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
    val fillStyle = MaterialTheme.typography.displayLarge.copy(
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
    )
    val strokeStyle = fillStyle.copy(
        color = Color.Black,
        drawStyle = Stroke(8f)
    )

    val channelNo = channelNoProvider()
    Box(modifier) {
        Text(
            text = channelNo,
            style = strokeStyle,
        )

        Text(
            text = channelNo,
            style = fillStyle
        )
    }
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