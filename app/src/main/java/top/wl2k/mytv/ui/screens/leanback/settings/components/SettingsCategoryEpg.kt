package top.wl2k.mytv.ui.screens.leanback.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import top.wl2k.mytv.data.repositories.epg.EpgRepository
import top.wl2k.mytv.data.utils.Constants
import top.wl2k.mytv.ui.screens.leanback.components.LeanbackQrcodeDialog
import top.wl2k.mytv.ui.screens.leanback.settings.LeanbackSettingsViewModel
import top.wl2k.mytv.ui.screens.leanback.toast.Toaster
import top.wl2k.mytv.ui.utils.HttpServer
import top.wl2k.mytv.ui.utils.handleLeanbackKeyEvents
import kotlin.math.max

@Composable
fun LeanbackSettingsCategoryEpg(
    modifier: Modifier = Modifier,
    settingsViewModel: LeanbackSettingsViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            LeanbackSettingsCategoryListItem(
                headlineContent = "启用节目单",
                supportingContent = "首次加载时可能会有跳帧风险",
                trailingContent = {
                    Switch(checked = settingsViewModel.epgEnable, onCheckedChange = null)
                },
                onSelected = {
                    settingsViewModel.epgEnable = !settingsViewModel.epgEnable
                },
            )
        }
        item {
            LeanbackSettingsCategoryListItem(
                headlineContent = "节目单刷新时间阈值",
                supportingContent = "短按增加1小时，长按设为0小时；时间不到${settingsViewModel.epgRefreshTimeThreshold}:00节目单将不会刷新",
                trailingContent = "${settingsViewModel.epgRefreshTimeThreshold}小时",
                onSelected = {
                    settingsViewModel.epgRefreshTimeThreshold =
                        (settingsViewModel.epgRefreshTimeThreshold + 1) % 12
                },
                onLongSelected = {
                    settingsViewModel.epgRefreshTimeThreshold = 0
                },
            )
        }
        item {
            var showDialog by remember {
                mutableStateOf(
                    false
                )
            }

            LeanbackSettingsCategoryListItem(
                headlineContent = "自定义节目单",
                supportingContent = if (settingsViewModel.epgXmlUrl != Constants.EPG_XML_URL)
                    settingsViewModel.epgXmlUrl else null,
                trailingContent = if (settingsViewModel.epgXmlUrl != Constants.EPG_XML_URL) "已启用" else "未启用",
                onSelected = { showDialog = true },
                remoteConfig = true,
            )

            LeanbackSettingsEpgSourceHistoryDialog(
                showDialogProvider = { showDialog },
                onDismissRequest = { showDialog = false },
                epgXmlUrlHistoryProvider = {
                    settingsViewModel.epgXmlUrlHistoryList.filter {
                        it != Constants.EPG_XML_URL
                    }.toImmutableList()
                },
                currentEpgXmlUrlProvider = { settingsViewModel.epgXmlUrl },
                onSelected = {
                    showDialog = false
                    if (settingsViewModel.epgXmlUrl != it) {
                        settingsViewModel.epgXmlUrl = it
                        coroutineScope.launch { EpgRepository().clearCache() }
                    }
                },
                onDeleted = {
                    settingsViewModel.epgXmlUrlHistoryList -= it
                }
            )
        }
        item {
            LeanbackSettingsCategoryListItem(
                headlineContent = "清除缓存",
                supportingContent = "短按清除节目单缓存文件",
                onSelected = {
                    coroutineScope.launch { EpgRepository().clearCache() }
                    Toaster.show("清除缓存成功")
                },
            )
        }
    }
}

@Composable
private fun LeanbackSettingsEpgSourceHistoryDialog(
    modifier: Modifier = Modifier,
    showDialogProvider: () -> Boolean = { false },
    onDismissRequest: () -> Unit = {},
    epgXmlUrlHistoryProvider: () -> ImmutableList<String> = { persistentListOf() },
    currentEpgXmlUrlProvider: () -> String = { Constants.EPG_XML_URL },
    onSelected: (String) -> Unit = {},
    onDeleted: (String) -> Unit = {},
) {
    val epgXmlUrlHistory = listOf(Constants.EPG_XML_URL) + epgXmlUrlHistoryProvider()
    val currentEpgXmlUrl = currentEpgXmlUrlProvider()

    if (showDialogProvider()) {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            confirmButton = { Text(text = "短按切换；长按删除历史记录") },
            title = { Text("历史节目单") },
            text = {
                var hasFocused by remember { mutableStateOf(false) }

                LazyListState(
                    max(
                        0,
                        epgXmlUrlHistory.indexOf(currentEpgXmlUrl) - 2
                    )
                )
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(epgXmlUrlHistory) { url ->
                        val focusRequester = remember { FocusRequester() }
                        var isFocused by remember {
                            mutableStateOf(
                                false
                            )
                        }

                        LaunchedEffect(Unit) {
                            if (url == currentEpgXmlUrl && !hasFocused) {
                                hasFocused = true
                                focusRequester.requestFocus()
                            }
                        }

                        ListItem(
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                                .handleLeanbackKeyEvents(
                                    onSelect = {
                                        if (isFocused) onSelected(url)
                                        else focusRequester.requestFocus()
                                    },
                                    onLongSelect = {
                                        if (isFocused) onDeleted(url)
                                        else focusRequester.requestFocus()
                                    }
                                ),
                            selected = currentEpgXmlUrl == url,
                            onClick = { },
                            headlineContent = {
                                androidx.tv.material3.Text(
                                    text = if (url == Constants.EPG_XML_URL) "默认节目单" else url,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = if (isFocused) Int.MAX_VALUE else 2,
                                )
                            },
                            trailingContent = {
                                if (currentEpgXmlUrl == url) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "checked",
                                    )
                                }
                            },
                        )
                    }
                    item {
                        val focusRequester = remember { FocusRequester() }
                        var isFocused by remember {
                            mutableStateOf(
                                false
                            )
                        }
                        var showDialog by remember {
                            mutableStateOf(
                                false
                            )
                        }

                        ListItem(
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                                .handleLeanbackKeyEvents(
                                    onSelect = {
                                        if (isFocused) showDialog = true
                                        else focusRequester.requestFocus()
                                    },
                                ),
                            selected = false,
                            onClick = {},
                            headlineContent = {
                                androidx.tv.material3.Text("添加其他节目单")
                            },
                        )

                        LeanbackQrcodeDialog(
                            text = HttpServer.serverUrl,
                            description = "扫码前往设置页面",
                            showDialogProvider = { showDialog },
                            onDismissRequest = { showDialog = false },
                        )
                    }
                }
            }
        )
    }
}
