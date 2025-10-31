package top.yogiczy.mytv.ui.screens.leanback.panel.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.ui.screens.leanback.video.player.LeanbackVideoPlayer
import top.yogiczy.mytv.ui.theme.LeanbackTheme

@Composable
fun LeanbackPanelPlayerInfo(
    modifier: Modifier = Modifier,
    metadataProvider: () -> LeanbackVideoPlayer.Metadata = { LeanbackVideoPlayer.Metadata() },
) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyLarge,
        LocalContentColor provides MaterialTheme.colorScheme.onBackground
    ) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PanelPlayerInfoResolution(
                resolutionProvider = {
                    val metadata = metadataProvider()
                    metadata.videoWidth to metadata.videoHeight
                }
            )
        }
    }
}

@Composable
private fun PanelPlayerInfoResolution(
    modifier: Modifier = Modifier,
    resolutionProvider: () -> Pair<Int, Int> = { 0 to 0 },
) {
    val resolution = resolutionProvider()

    Text(
        text = "分辨率：${resolution.first}×${resolution.second}",
        modifier = modifier,
    )
}

@Preview
@Composable
private fun LeanbackPanelPlayerInfoPreview() {
    LeanbackTheme {
        LeanbackPanelPlayerInfo(
            metadataProvider = {
                LeanbackVideoPlayer.Metadata(
                    videoWidth = 1920,
                    videoHeight = 1080,
                )
            },
        )
    }
}
