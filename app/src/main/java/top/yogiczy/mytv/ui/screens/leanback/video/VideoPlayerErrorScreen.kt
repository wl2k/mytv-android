package top.yogiczy.mytv.ui.screens.leanback.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.ui.theme.LeanbackTheme

@Composable
fun LeanbackVideoPlayerErrorScreen(
    modifier: Modifier = Modifier,
    errorProvider: () -> String?,
    countdownProvider: () -> Int = { 0 },
) {
    Box(modifier = modifier.fillMaxSize()) {
        errorProvider()?.let {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.medium,
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "播放失败",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error,
                )

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.8f),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${countdownProvider()} 秒后切换到下一个频道...",
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Preview(device = TV_720p)
@Composable
private fun LeanbackVideoPlayerErrorScreenPreview() {
    LeanbackTheme {
        LeanbackVideoPlayerErrorScreen(
            errorProvider = { "ERROR_CODE_BEHIND_LIVE_WINDOW" },
            countdownProvider = { 3 },
        )
    }
}
