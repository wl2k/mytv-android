package top.wl2k.mytv.ui.screens.leanback.main.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.wl2k.mytv.data.entities.Iptv
import top.wl2k.mytv.data.entities.IptvGroupList
import top.wl2k.mytv.data.entities.IptvGroupList.Companion.iptvIdx
import top.wl2k.mytv.data.entities.IptvGroupList.Companion.iptvList
import top.wl2k.mytv.data.utils.Constants
import top.wl2k.mytv.ui.screens.leanback.video.LeanbackVideoPlayerState
import top.wl2k.mytv.ui.screens.leanback.video.rememberLeanbackVideoPlayerState
import top.wl2k.mytv.ui.utils.SP
import top.wl2k.mytv.utils.Loggable
import kotlin.math.max

@Stable
class LeanbackMainContentState(
    coroutineScope: CoroutineScope,
    private val videoPlayerState: LeanbackVideoPlayerState,
    private val iptvGroupList: IptvGroupList,
) : Loggable() {
    private var _currentIptv by mutableStateOf(Iptv())
    val currentIptv get() = _currentIptv

    private var _currentIptvUrlIdx by mutableIntStateOf(0)
    val currentIptvUrlIdx get() = _currentIptvUrlIdx

    private var _isPanelVisible by mutableStateOf(false)
    var isPanelVisible
        get() = _isPanelVisible
        set(value) {
            _isPanelVisible = value
        }

    private var _isSettingsVisible by mutableStateOf(false)
    var isSettingsVisible
        get() = _isSettingsVisible
        set(value) {
            _isSettingsVisible = value
        }

    private var _isTempPanelVisible by mutableStateOf(false)
    var isTempPanelVisible
        get() = _isTempPanelVisible
        set(value) {
            _isTempPanelVisible = value
        }

    private var _isQuickPanelVisible by mutableStateOf(false)
    var isQuickPanelVisible
        get() = _isQuickPanelVisible
        set(value) {
            _isQuickPanelVisible = value
        }

    private var needRetryAllUrls = false

    init {
        changeCurrentIptv(iptvGroupList.iptvList.getOrElse(SP.iptvLastIptvIdx) {
            iptvGroupList.firstOrNull()?.iptvList?.firstOrNull() ?: Iptv()
        })

        videoPlayerState.onReady {
            coroutineScope.launch {
                val name = _currentIptv.name
                val urlIdx = _currentIptvUrlIdx
                delay(Constants.UI_TEMP_PANEL_SCREEN_SHOW_DURATION)
                if (name == _currentIptv.name && urlIdx == _currentIptvUrlIdx) {
                    _isTempPanelVisible = false
                }
            }

            // 记忆可播放的域名
            SP.iptvPlayableHostList += getHostFrom(_currentIptv.urlList[_currentIptvUrlIdx])
            needRetryAllUrls = false
        }

        videoPlayerState.onError {
            if (_currentIptvUrlIdx >= _currentIptv.urlList.size - 1) {
                // 已经是最后一个 URL，证明频道的所有 URL 都不可用，再重试一轮；如果仍然不可用，再切换到下一个频道
                if (!needRetryAllUrls) {
                    needRetryAllUrls = true
                    Log.i("MainContentState", "已尝试所有 URL，再次尝试当前频道")
                    changeCurrentIptv(_currentIptv, 0)
                }
            } else {
                changeCurrentIptv(_currentIptv, _currentIptvUrlIdx + 1)
            }

            // 从记忆中删除不可播放的域名
            SP.iptvPlayableHostList -= getHostFrom(_currentIptv.urlList[_currentIptvUrlIdx])
        }

        videoPlayerState.onCutoff {
            changeCurrentIptv(_currentIptv, _currentIptvUrlIdx)
        }
    }

    private fun getPrevIptv(): Iptv {
        val currentIndex = iptvGroupList.iptvIdx(_currentIptv)
        return iptvGroupList.iptvList.getOrElse(currentIndex - 1) {
            iptvGroupList.lastOrNull()?.iptvList?.lastOrNull() ?: Iptv()
        }
    }

    private fun getNextIptv(): Iptv {
        val currentIndex = iptvGroupList.iptvIdx(_currentIptv)
        return iptvGroupList.iptvList.getOrElse(currentIndex + 1) {
            iptvGroupList.firstOrNull()?.iptvList?.firstOrNull() ?: Iptv()
        }
    }

    fun changeCurrentIptv(iptv: Iptv, urlIdx: Int? = null) {
        _isPanelVisible = false

        if (iptv == _currentIptv && urlIdx == null) return
        if (iptv == _currentIptv && urlIdx != _currentIptvUrlIdx)
            SP.iptvPlayableHostList -= getHostFrom(_currentIptv.urlList[_currentIptvUrlIdx])


        if (iptv != _currentIptv)
            needRetryAllUrls = false

        _isTempPanelVisible = true

        _currentIptv = iptv
        SP.iptvLastIptvIdx = iptvGroupList.iptvIdx(_currentIptv)

        _currentIptvUrlIdx = if (urlIdx == null) {
            // 优先从记忆中选择可播放的域名
            max(0, _currentIptv.urlList.indexOfFirst {
                SP.iptvPlayableHostList.contains(getHostFrom(it))
            })
        } else {
            (urlIdx + _currentIptv.urlList.size) % _currentIptv.urlList.size
        }

        val url = iptv.urlList[_currentIptvUrlIdx]
        log.d("播放${iptv.name} (${_currentIptvUrlIdx + 1}/${_currentIptv.urlList.size}): $url")

        videoPlayerState.prepare(url)
    }

    fun changeCurrentIptvToPrev() {
        changeCurrentIptv(getPrevIptv())
    }

    fun changeCurrentIptvToNext() {
        changeCurrentIptv(getNextIptv())
    }
}

@Composable
fun rememberLeanbackMainContentState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    videoPlayerState: LeanbackVideoPlayerState = rememberLeanbackVideoPlayerState(),
    iptvGroupList: IptvGroupList = IptvGroupList(),
) = remember {
    LeanbackMainContentState(
        coroutineScope = coroutineScope,
        videoPlayerState = videoPlayerState,
        iptvGroupList = iptvGroupList,
    )
}

private fun getHostFrom(url: String): String {
    return url.split("://").getOrElse(1) { url }
}