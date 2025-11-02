package top.wl2k.mytv.ui.screens.leanback.panel.components

import android.net.TrafficStats
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import top.wl2k.mytv.data.entities.EpgProgrammeCurrent
import top.wl2k.mytv.data.entities.Iptv
import top.wl2k.mytv.ui.theme.LeanbackTheme
import java.text.DecimalFormat

@Composable
fun LeanbackPanelIptvInfo(
    modifier: Modifier = Modifier,
    channelNoProvider: () -> String = { "" },
    iptvProvider: () -> Iptv = { Iptv() },
    iptvUrlIdxProvider: () -> Int = { 0 },
    currentProgrammesProvider: () -> EpgProgrammeCurrent? = { null },
) {
    val iptv = iptvProvider()
    val iptvUrlIdx = iptvUrlIdxProvider()
    val currentProgrammes = currentProgrammesProvider()

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (channelNoProvider().isNotEmpty()) {
                LeanbackPanelChannelNo(channelNoProvider = channelNoProvider)
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = iptv.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterVertically),
                maxLines = 1,
            )

            Spacer(modifier = Modifier.width(8.dp))

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.labelMedium,
                LocalContentColor provides LocalContentColor.current.copy(alpha = 0.8f),
            ) {
                val textModifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        LocalContentColor.current.copy(alpha = 0.3f),
                        MaterialTheme.shapes.extraSmall,
                    )
                    .padding(vertical = 2.dp, horizontal = 4.dp)

                // 多线路标识
                if (iptv.urlList.size > 1) {
                    Text(
                        text = "线路 ${iptvUrlIdx + 1}/${iptv.urlList.size}",
                        modifier = textModifier,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                PanelPlayerInfoNetSpeed(modifier = textModifier)
            }
        }

        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge,
            LocalContentColor provides LocalContentColor.current.copy(alpha = 0.8f),
        ) {
            if (!currentProgrammes?.now?.title.isNullOrEmpty()) {
                Text(
                    text = "正在播放：${currentProgrammes.now.title}",
                    maxLines = 1,
                )
            }
            if (!currentProgrammes?.next?.title.isNullOrEmpty()) {
                Text(
                    text = "稍后播放：${currentProgrammes.next.title}",
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LeanbackPanelIptvInfoPreview() {
    LeanbackTheme {
        LeanbackPanelIptvInfo(
            iptvProvider = { Iptv.EXAMPLE },
            iptvUrlIdxProvider = { 1 },
            currentProgrammesProvider = { EpgProgrammeCurrent.EXAMPLE },
        )
    }
}

@Preview
@Composable
private fun LeanbackPanelIptvInfoWithChannelNoPreview() {
    LeanbackTheme {
        LeanbackPanelIptvInfo(
            channelNoProvider = { "1" },
            iptvProvider = { Iptv.EXAMPLE },
            iptvUrlIdxProvider = { 1 },
            currentProgrammesProvider = { EpgProgrammeCurrent.EXAMPLE },
        )
    }
}

@Composable
private fun PanelPlayerInfoNetSpeed(
    modifier: Modifier = Modifier,
    netSpeed: Long = rememberNetSpeed(),
) {
    Text(
        text = if (netSpeed < 1024 * 999) "${netSpeed / 1024} KB/s"
        else "${DecimalFormat("#.#").format(netSpeed / 1024 / 1024f)} MB/s",
        modifier = modifier,
    )
}

@Composable
private fun rememberNetSpeed(): Long {
    var netSpeed by remember { mutableLongStateOf(0) }

    LaunchedEffect(Unit) {
        var lastTotalRxBytes = TrafficStats.getTotalRxBytes()
        var lastTimeStamp = System.currentTimeMillis()

        while (true) {
            delay(1000)
            val nowTotalRxBytes = TrafficStats.getTotalRxBytes()
            val nowTimeStamp = System.currentTimeMillis()
            val speed = (nowTotalRxBytes - lastTotalRxBytes) / (nowTimeStamp - lastTimeStamp) * 1000
            lastTimeStamp = nowTimeStamp
            lastTotalRxBytes = nowTotalRxBytes

            netSpeed = speed
        }
    }

    return netSpeed
}

@Preview
@Composable
private fun LeanbackPanelPlayerInfoNetSpeedPreview() {
    LeanbackTheme {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            PanelPlayerInfoNetSpeed()
            PanelPlayerInfoNetSpeed(netSpeed = 54321)
            PanelPlayerInfoNetSpeed(netSpeed = 1222 * 1222)
        }
    }
}