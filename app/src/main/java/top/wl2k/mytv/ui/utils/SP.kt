package top.wl2k.mytv.ui.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import top.wl2k.mytv.data.utils.Constants

/**
 * 应用配置存储
 */
object SP {
    private const val SP_NAME = "mytv"
    private const val SP_MODE = Context.MODE_PRIVATE
    private lateinit var sp: SharedPreferences

    fun getInstance(context: Context): SharedPreferences =
        context.getSharedPreferences(SP_NAME, SP_MODE)

    fun init(context: Context) {
        sp = getInstance(context)
    }

    enum class KEY {
        /** ==================== 调试 ==================== */
        /** 显示fps */
        DEBUG_SHOW_FPS,

        /** 播放器详细信息 */
        DEBUG_SHOW_VIDEO_PLAYER_METADATA,

        /** ==================== 直播源 ==================== */
        /** 上一次直播源序号 */
        IPTV_LAST_IPTV_IDX,

        /** 换台反转 */
        IPTV_CHANNEL_CHANGE_FLIP,

        /** 直播源精简 */
        IPTV_SOURCE_SIMPLIFY,

        /** 直播源url */
        IPTV_SOURCE_URL,

        /** 直播源缓存时间（毫秒） */
        IPTV_SOURCE_CACHE_TIME,

        /** 直播源可播放host列表 */
        IPTV_PLAYABLE_HOST_LIST,

        /** 直播源历史列表 */
        IPTV_SOURCE_URL_HISTORY_LIST,

        /** 是否启用数字选台 */
        IPTV_CHANNEL_NO_SELECT_ENABLE,

        /** 是否启用直播源频道收藏 */
        IPTV_CHANNEL_FAVORITE_ENABLE,

        /** 显示直播源频道收藏列表 */
        IPTV_CHANNEL_FAVORITE_LIST_VISIBLE,

        /** 直播源频道收藏列表 */
        IPTV_CHANNEL_FAVORITE_LIST,

        /** ==================== 节目单 ==================== */
        /** 启用节目单 */
        EPG_ENABLE,

        /** 节目单 xml url */
        EPG_XML_URL,

        /** 节目单刷新时间阈值（小时） */
        EPG_REFRESH_TIME_THRESHOLD,

        /** 节目单历史列表 */
        EPG_XML_URL_HISTORY_LIST,

        /** ==================== 界面 ==================== */
        /** 显示节目进度 */
        UI_SHOW_EPG_PROGRAMME_PROGRESS,

        /** 界面密度缩放比例 */
        UI_DENSITY_SCALE_RATIO,

        /** 界面字体缩放比例 */
        UI_FONT_SCALE_RATIO,

        /** 时间显示模式 */
        UI_TIME_SHOW_MODE,

        /** ==================== 播放器 ==================== */
        /** 播放器 自定义ua */
        VIDEO_PLAYER_USER_AGENT,

        /** 播放器 加载超时 */
        VIDEO_PLAYER_LOAD_TIMEOUT,

        /** 播放器 画面比例 */
        VIDEO_PLAYER_ASPECT_RATIO,

        /** 开机自启 */
        APP_BOOT_LAUNCH,
    }

    /** ==================== 调试 ==================== */
    /** 显示fps */
    var debugShowFps: Boolean
        get() = sp.getBoolean(KEY.DEBUG_SHOW_FPS.name, false)
        set(value) = sp.edit { putBoolean(KEY.DEBUG_SHOW_FPS.name, value) }

    /** 播放器详细信息 */
    var debugShowVideoPlayerMetadata: Boolean
        get() = sp.getBoolean(KEY.DEBUG_SHOW_VIDEO_PLAYER_METADATA.name, false)
        set(value) = sp.edit { putBoolean(KEY.DEBUG_SHOW_VIDEO_PLAYER_METADATA.name, value) }

    /** ==================== 直播源 ==================== */
    /** 上一次直播源序号 */
    var iptvLastIptvIdx: Int
        get() = sp.getInt(KEY.IPTV_LAST_IPTV_IDX.name, 0)
        set(value) = sp.edit { putInt(KEY.IPTV_LAST_IPTV_IDX.name, value) }

    /** 换台反转 */
    var iptvChannelChangeFlip: Boolean
        get() = sp.getBoolean(KEY.IPTV_CHANNEL_CHANGE_FLIP.name, false)
        set(value) = sp.edit { putBoolean(KEY.IPTV_CHANNEL_CHANGE_FLIP.name, value) }

    /** 直播源精简 */
    var iptvSourceSimplify: Boolean
        get() = sp.getBoolean(KEY.IPTV_SOURCE_SIMPLIFY.name, false)
        set(value) = sp.edit { putBoolean(KEY.IPTV_SOURCE_SIMPLIFY.name, value) }

    /** 直播源 url */
    var iptvSourceUrl: String
        get() = (sp.getString(KEY.IPTV_SOURCE_URL.name, "")
            ?: "").ifBlank { Constants.IPTV_SOURCE_URL }
        set(value) = sp.edit { putString(KEY.IPTV_SOURCE_URL.name, value) }

    /** 直播源缓存时间（毫秒） */
    var iptvSourceCacheTime: Long
        get() = sp.getLong(KEY.IPTV_SOURCE_CACHE_TIME.name, Constants.IPTV_SOURCE_CACHE_TIME)
        set(value) = sp.edit { putLong(KEY.IPTV_SOURCE_CACHE_TIME.name, value) }

    /** 直播源可播放host列表 */
    var iptvPlayableHostList: Set<String>
        get() = sp.getStringSet(KEY.IPTV_PLAYABLE_HOST_LIST.name, emptySet()) ?: emptySet()
        set(value) = sp.edit { putStringSet(KEY.IPTV_PLAYABLE_HOST_LIST.name, value) }

    /** 直播源历史列表 */
    var iptvSourceUrlHistoryList: Set<String>
        get() = sp.getStringSet(KEY.IPTV_SOURCE_URL_HISTORY_LIST.name, emptySet()) ?: emptySet()
        set(value) = sp.edit { putStringSet(KEY.IPTV_SOURCE_URL_HISTORY_LIST.name, value) }

    /** 是否启用数字选台 */
    var iptvChannelNoSelectEnable: Boolean
        get() = sp.getBoolean(KEY.IPTV_CHANNEL_NO_SELECT_ENABLE.name, true)
        set(value) = sp.edit { putBoolean(KEY.IPTV_CHANNEL_NO_SELECT_ENABLE.name, value) }

    /** 是否启用直播源频道收藏 */
    var iptvChannelFavoriteEnable: Boolean
        get() = sp.getBoolean(KEY.IPTV_CHANNEL_FAVORITE_ENABLE.name, true)
        set(value) = sp.edit { putBoolean(KEY.IPTV_CHANNEL_FAVORITE_ENABLE.name, value) }

    /** 显示直播源频道收藏列表 */
    var iptvChannelFavoriteListVisible: Boolean
        get() = sp.getBoolean(KEY.IPTV_CHANNEL_FAVORITE_LIST_VISIBLE.name, false)
        set(value) = sp.edit {
            putBoolean(KEY.IPTV_CHANNEL_FAVORITE_LIST_VISIBLE.name, value)
        }

    /** 直播源频道收藏列表 */
    var iptvChannelFavoriteList: Set<String>
        get() = sp.getStringSet(KEY.IPTV_CHANNEL_FAVORITE_LIST.name, emptySet()) ?: emptySet()
        set(value) = sp.edit { putStringSet(KEY.IPTV_CHANNEL_FAVORITE_LIST.name, value) }

    /** ==================== 节目单 ==================== */
    /** 启用节目单 */
    var epgEnable: Boolean
        get() = sp.getBoolean(KEY.EPG_ENABLE.name, true)
        set(value) = sp.edit { putBoolean(KEY.EPG_ENABLE.name, value) }

    /** 节目单 xml url */
    var epgXmlUrl: String
        get() = (sp.getString(KEY.EPG_XML_URL.name, "") ?: "").ifBlank { Constants.EPG_XML_URL }
        set(value) = sp.edit { putString(KEY.EPG_XML_URL.name, value) }

    /** 节目单刷新时间阈值（小时） */
    var epgRefreshTimeThreshold: Int
        get() = sp.getInt(KEY.EPG_REFRESH_TIME_THRESHOLD.name, Constants.EPG_REFRESH_TIME_THRESHOLD)
        set(value) = sp.edit { putInt(KEY.EPG_REFRESH_TIME_THRESHOLD.name, value) }

    /** 节目单历史列表 */
    var epgXmlUrlHistoryList: Set<String>
        get() = sp.getStringSet(KEY.EPG_XML_URL_HISTORY_LIST.name, emptySet()) ?: emptySet()
        set(value) = sp.edit { putStringSet(KEY.EPG_XML_URL_HISTORY_LIST.name, value) }

    /** ==================== 界面 ==================== */
    /** 显示节目进度 */
    var uiShowEpgProgrammeProgress: Boolean
        get() = sp.getBoolean(KEY.UI_SHOW_EPG_PROGRAMME_PROGRESS.name, true)
        set(value) = sp.edit { putBoolean(KEY.UI_SHOW_EPG_PROGRAMME_PROGRESS.name, value) }

    /** 界面密度缩放比例 */
    var uiDensityScaleRatio: Float
        get() = sp.getFloat(KEY.UI_DENSITY_SCALE_RATIO.name, 1f)
        set(value) = sp.edit { putFloat(KEY.UI_DENSITY_SCALE_RATIO.name, value) }

    /** 界面字体缩放比例 */
    var uiFontScaleRatio: Float
        get() = sp.getFloat(KEY.UI_FONT_SCALE_RATIO.name, 1f)
        set(value) = sp.edit { putFloat(KEY.UI_FONT_SCALE_RATIO.name, value) }

    /** 时间显示模式 */
    var uiTimeShowMode: UiTimeShowMode
        get() = UiTimeShowMode.fromValue(sp.getInt(KEY.UI_TIME_SHOW_MODE.name, 0))
        set(value) = sp.edit { putInt(KEY.UI_TIME_SHOW_MODE.name, value.value) }

    /** ==================== 播放器 ==================== */
    /** 播放器 自定义ua */
    var videoPlayerUserAgent: String
        get() = (sp.getString(KEY.VIDEO_PLAYER_USER_AGENT.name, "") ?: "").ifBlank {
            Constants.VIDEO_PLAYER_USER_AGENT
        }
        set(value) = sp.edit { putString(KEY.VIDEO_PLAYER_USER_AGENT.name, value) }

    /** 播放器 加载超时 */
    var videoPlayerLoadTimeout: Long
        get() = sp.getLong(KEY.VIDEO_PLAYER_LOAD_TIMEOUT.name, Constants.VIDEO_PLAYER_LOAD_TIMEOUT)
        set(value) = sp.edit { putLong(KEY.VIDEO_PLAYER_LOAD_TIMEOUT.name, value) }

    /** 播放器 画面比例 */
    var videoPlayerAspectRatio: VideoPlayerAspectRatio
        get() = VideoPlayerAspectRatio.fromValue(
            sp.getInt(KEY.VIDEO_PLAYER_ASPECT_RATIO.name, VideoPlayerAspectRatio.ORIGINAL.value)
        )
        set(value) = sp.edit { putInt(KEY.VIDEO_PLAYER_ASPECT_RATIO.name, value.value) }

    /** 开机自启 */
    var appBootLaunch: Boolean
        get() = sp.getBoolean(KEY.APP_BOOT_LAUNCH.name, false)
        set(value) = sp.edit { putBoolean(KEY.APP_BOOT_LAUNCH.name, value) }

    enum class UiTimeShowMode(val value: Int) {
        /** 隐藏 */
        HIDDEN(0),

        /** 常显 */
        ALWAYS(1),

        /** 整点 */
        EVERY_HOUR(2),

        /** 半点 */
        HALF_HOUR(3);

        companion object {
            fun fromValue(value: Int): UiTimeShowMode {
                return entries.firstOrNull { it.value == value } ?: ALWAYS
            }
        }
    }

    enum class VideoPlayerAspectRatio(val value: Int) {
        /** 原始 */
        ORIGINAL(0),

        /** 16:9 */
        SIXTEEN_NINE(1),

        /** 4:3 */
        FOUR_THREE(2),

        /** 自动拉伸 */
        AUTO(3);

        companion object {
            fun fromValue(value: Int): VideoPlayerAspectRatio {
                return entries.firstOrNull { it.value == value } ?: ORIGINAL
            }
        }
    }
}