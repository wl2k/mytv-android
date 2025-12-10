package top.wl2k.mytv.ui.screens.leanback.video

import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import top.wl2k.mytv.ui.rememberLeanbackChildPadding
import top.wl2k.mytv.ui.screens.leanback.video.components.LeanbackVideoPlayerMetadata
import kotlin.time.Duration.Companion.seconds

@Composable
fun LeanbackVideoScreen(
    modifier: Modifier = Modifier,
    state: LeanbackVideoPlayerState = rememberLeanbackVideoPlayerState(),
    showMetadataProvider: () -> Boolean = { false },
    onError: (LeanbackVideoPlayerState) -> Unit = {},
) {
    val context = LocalContext.current
    val childPadding = rememberLeanbackChildPadding()
    var countdown by remember { mutableIntStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .align(Alignment.Center)
                .aspectRatio(state.aspectRatio),
            factory = {
                // PlayerView 切换视频时黑屏闪烁，使用 SurfaceView 代替
                SurfaceView(context)
            },
            update = {
                state.setVideoSurfaceView(it)
            },
        )

        LaunchedEffect(state.error) {
            if (state.error != null) {
                countdown = 3
                while (countdown > 0) {
                    delay(1.seconds)
                    countdown--
                }
                onError(state)
            }
        }

        LeanbackVideoPlayerErrorScreen(
            errorProvider = { state.error },
            countdownProvider = { countdown },
        )

        if (showMetadataProvider()) {
            LeanbackVideoPlayerMetadata(
                modifier = Modifier.padding(start = childPadding.start, top = childPadding.top),
                metadata = state.metadata,
            )
        }
    }
}
