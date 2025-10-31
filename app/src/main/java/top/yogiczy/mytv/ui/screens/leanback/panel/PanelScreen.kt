package top.yogiczy.mytv.ui.screens.leanback.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import top.yogiczy.mytv.data.entities.EpgList
import top.yogiczy.mytv.data.entities.EpgList.Companion.currentProgrammes
import top.yogiczy.mytv.data.entities.Iptv
import top.yogiczy.mytv.data.entities.IptvGroupList
import top.yogiczy.mytv.data.entities.IptvGroupList.Companion.iptvIdx
import top.yogiczy.mytv.data.entities.IptvGroupList.Companion.iptvList
import top.yogiczy.mytv.data.entities.IptvList
import top.yogiczy.mytv.data.utils.Constants
import top.yogiczy.mytv.ui.rememberLeanbackChildPadding
import top.yogiczy.mytv.ui.screens.leanback.panel.components.LeanbackPanelChannelNo
import top.yogiczy.mytv.ui.screens.leanback.panel.components.LeanbackPanelDateTime
import top.yogiczy.mytv.ui.screens.leanback.panel.components.LeanbackPanelIptvFavoriteList
import top.yogiczy.mytv.ui.screens.leanback.panel.components.LeanbackPanelIptvGroupList
import top.yogiczy.mytv.ui.screens.leanback.panel.components.LeanbackPanelIptvInfo
import top.yogiczy.mytv.ui.screens.leanback.panel.components.LeanbackPanelPlayerInfo
import top.yogiczy.mytv.ui.screens.leanback.toast.LeanbackToastState
import top.yogiczy.mytv.ui.screens.leanback.video.player.LeanbackVideoPlayer
import top.yogiczy.mytv.ui.theme.LeanbackTheme

@Composable
fun LeanbackPanelScreen(
    modifier: Modifier = Modifier,
    iptvGroupListProvider: () -> IptvGroupList = { IptvGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    currentIptvProvider: () -> Iptv = { Iptv() },
    currentIptvUrlIdxProvider: () -> Int = { 0 },
    videoPlayerMetadataProvider: () -> LeanbackVideoPlayer.Metadata = { LeanbackVideoPlayer.Metadata() },
    showProgrammeProgressProvider: () -> Boolean = { false },
    iptvFavoriteEnableProvider: () -> Boolean = { true },
    iptvFavoriteListProvider: () -> ImmutableList<String> = { persistentListOf() },
    iptvFavoriteListVisibleProvider: () -> Boolean = { false },
    onIptvFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onIptvSelected: (Iptv) -> Unit = {},
    onIptvFavoriteToggle: (Iptv) -> Unit = {},
    onClose: () -> Unit = {},
    autoCloseState: PanelAutoCloseState = rememberPanelAutoCloseState(
        timeout = Constants.UI_SCREEN_AUTO_CLOSE_DELAY,
        onTimeout = onClose,
    ),
) {
    LaunchedEffect(Unit) {
        autoCloseState.active()
    }

    val channelNoProvider = {
        val idx = iptvGroupListProvider().iptvIdx(currentIptvProvider())
        (idx + 1).toString()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            .pointerInput(Unit) { detectTapGestures(onTap = { onClose() }) },
    ) {
        LeanbackPanelScreenTopRight(channelNoProvider = channelNoProvider)

        LeanbackPanelScreenBottom(
            iptvGroupListProvider = iptvGroupListProvider,
            epgListProvider = epgListProvider,
            channelNoProvider = channelNoProvider,
            currentIptvProvider = currentIptvProvider,
            currentIptvUrlIdxProvider = currentIptvUrlIdxProvider,
            videoPlayerMetadataProvider = videoPlayerMetadataProvider,
            showProgrammeProgressProvider = showProgrammeProgressProvider,
            iptvFavoriteEnableProvider = iptvFavoriteEnableProvider,
            iptvFavoriteListProvider = iptvFavoriteListProvider,
            iptvFavoriteListVisibleProvider = iptvFavoriteListVisibleProvider,
            onIptvFavoriteListVisibleChange = onIptvFavoriteListVisibleChange,
            onIptvSelected = onIptvSelected,
            onIptvFavoriteToggle = onIptvFavoriteToggle,
            onUserAction = { autoCloseState.active() },
        )
    }
}

@Composable
fun LeanbackPanelScreenTopRight(
    modifier: Modifier = Modifier,
    channelNoProvider: () -> String = { "" },
) {
    val childPadding = rememberLeanbackChildPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = childPadding.top, end = childPadding.end),
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            LeanbackPanelChannelNo(
                modifier = Modifier.align(Alignment.End),
                channelNoProvider = channelNoProvider
            )
            LeanbackPanelDateTime()
        }
    }
}

@Composable
private fun LeanbackPanelScreenBottom(
    modifier: Modifier = Modifier,
    iptvGroupListProvider: () -> IptvGroupList = { IptvGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    channelNoProvider: () -> String = { "" },
    currentIptvProvider: () -> Iptv = { Iptv() },
    currentIptvUrlIdxProvider: () -> Int = { 0 },
    videoPlayerMetadataProvider: () -> LeanbackVideoPlayer.Metadata = { LeanbackVideoPlayer.Metadata() },
    showProgrammeProgressProvider: () -> Boolean = { false },
    iptvFavoriteEnableProvider: () -> Boolean = { true },
    iptvFavoriteListProvider: () -> ImmutableList<String> = { persistentListOf() },
    iptvFavoriteListVisibleProvider: () -> Boolean = { false },
    onIptvFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onIptvSelected: (Iptv) -> Unit = {},
    onIptvFavoriteToggle: (Iptv) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val childPadding = rememberLeanbackChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LeanbackPanelIptvInfo(
                modifier = Modifier.padding(start = childPadding.start),
                channelNoProvider = channelNoProvider,
                iptvProvider = currentIptvProvider,
                iptvUrlIdxProvider = currentIptvUrlIdxProvider,
                currentProgrammesProvider = {
                    epgListProvider().currentProgrammes(currentIptvProvider())
                },
            )

            LeanbackPanelPlayerInfo(
                modifier = Modifier.padding(start = childPadding.start),
                metadataProvider = videoPlayerMetadataProvider
            )

            LeanbackPanelScreenBottomIptvList(
                iptvGroupListProvider = iptvGroupListProvider,
                epgListProvider = epgListProvider,
                currentIptvProvider = currentIptvProvider,
                showProgrammeProgressProvider = showProgrammeProgressProvider,
                iptvFavoriteEnableProvider = iptvFavoriteEnableProvider,
                iptvFavoriteListProvider = iptvFavoriteListProvider,
                iptvFavoriteListVisibleProvider = iptvFavoriteListVisibleProvider,
                onIptvFavoriteListVisibleChange = onIptvFavoriteListVisibleChange,
                onIptvSelected = onIptvSelected,
                onIptvFavoriteToggle = onIptvFavoriteToggle,
                onUserAction = onUserAction,
            )
        }
    }
}

@Composable
fun LeanbackPanelScreenBottomIptvList(
    modifier: Modifier = Modifier,
    iptvGroupListProvider: () -> IptvGroupList = { IptvGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    currentIptvProvider: () -> Iptv = { Iptv() },
    showProgrammeProgressProvider: () -> Boolean = { false },
    iptvFavoriteEnableProvider: () -> Boolean = { true },
    iptvFavoriteListProvider: () -> ImmutableList<String> = { persistentListOf() },
    iptvFavoriteListVisibleProvider: () -> Boolean = { false },
    onIptvFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onIptvSelected: (Iptv) -> Unit = {},
    onIptvFavoriteToggle: (Iptv) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val iptvFavoriteEnable = iptvFavoriteEnableProvider()
    var favoriteListVisible by remember { mutableStateOf(iptvFavoriteListVisibleProvider()) }

    Box(modifier = modifier.height(150.dp)) {
        if (favoriteListVisible)
            LeanbackPanelIptvFavoriteList(
                iptvListProvider = {
                    IptvList(
                        iptvGroupListProvider().iptvList
                            .filter { iptvFavoriteListProvider().contains(it.channelName) })
                },
                epgListProvider = epgListProvider,
                currentIptvProvider = currentIptvProvider,
                showProgrammeProgressProvider = showProgrammeProgressProvider,
                onIptvSelected = onIptvSelected,
                onIptvFavoriteToggle = onIptvFavoriteToggle,
                onClose = {
                    favoriteListVisible = false
                    onIptvFavoriteListVisibleChange(false)
                },
                onUserAction = onUserAction,
            )
        else
            LeanbackPanelIptvGroupList(
                iptvGroupListProvider = iptvGroupListProvider,
                epgListProvider = epgListProvider,
                currentIptvProvider = currentIptvProvider,
                showProgrammeProgressProvider = showProgrammeProgressProvider,
                onIptvSelected = onIptvSelected,
                onIptvFavoriteToggle = onIptvFavoriteToggle,
                onToFavorite = {
                    if (!iptvFavoriteEnable) return@LeanbackPanelIptvGroupList

                    val favoriteList = iptvGroupListProvider().iptvList
                        .filter { iptvFavoriteListProvider().contains(it.channelName) }

                    if (favoriteList.isNotEmpty()) {
                        favoriteListVisible = true
                        onIptvFavoriteListVisibleChange(true)
                    } else {
                        LeanbackToastState.I.showToast("无收藏")
                    }
                },
                onUserAction = onUserAction,
            )
    }
}

@Preview(device = TV_720p)
@Composable
private fun LeanbackPanelScreenTopRightPreview() {
    LeanbackTheme {
        LeanbackPanelScreenTopRight(
            channelNoProvider = { "10" },
        )
    }
}

@Preview(device = TV_720p)
@Composable
private fun LeanbackPanelScreenPreview() {
    LeanbackTheme {
        LeanbackPanelScreenBottom(
            videoPlayerMetadataProvider = {
                LeanbackVideoPlayer.Metadata(
                    videoWidth = 1280,
                    videoHeight = 720,
                )
            },
            channelNoProvider = { "1" },
            currentIptvProvider = { Iptv.EXAMPLE },
            currentIptvUrlIdxProvider = { 0 },
            iptvGroupListProvider = { IptvGroupList.EXAMPLE },
        )
    }
}
