package top.yogiczy.mytv.data.utils

/**
 * 常量
 */
object Constants {
    /**
     * 应用 代码仓库
     */
    const val APP_REPO = "https://github.com/wl2k/mytv-android"

    /**
     * IPTV源地址
     */
    const val IPTV_SOURCE_URL = "http://192.0.2.1/live.m3u"

    /**
     * IPTV源缓存时间（毫秒）
     */
    const val IPTV_SOURCE_CACHE_TIME = 1000 * 60 * 60 * 24L // 24小时

    /**
     * 节目单XML地址
     */
    const val EPG_XML_URL = "https://epg.112114.xyz/pp.xml.gz"

    /**
     * 节目单刷新时间阈值（小时）
     */
    const val EPG_REFRESH_TIME_THRESHOLD = 2 // 不到2点不刷新

    /**
     * HTTP请求重试次数
     */
    const val HTTP_RETRY_COUNT = 10L

    /**
     * HTTP请求重试间隔时间（毫秒）
     */
    const val HTTP_RETRY_INTERVAL = 3000L

    /**
     * 播放器 user-agent
     */
    const val VIDEO_PLAYER_USER_AGENT = "okhttp/${okhttp3.OkHttp.VERSION}"

    /**
     * 日志历史最大保留条数
     */
    const val LOG_HISTORY_MAX_SIZE = 50

    /**
     * 播放器加载超时
     */
    const val VIDEO_PLAYER_LOAD_TIMEOUT = 1000L * 5 // 5秒

    /**
     * 界面 超时未操作自动关闭界面
     */
    const val UI_SCREEN_AUTO_CLOSE_DELAY = 1000L * 15 // 15秒

    /**
     * 界面 时间显示前后范围
     */
    const val UI_TIME_SHOW_RANGE = 1000L * 30 // 前后30秒

    /**
     * 界面 临时面板界面显示时间
     */
    const val UI_TEMP_PANEL_SCREEN_SHOW_DURATION = 1500L // 1.5秒
}