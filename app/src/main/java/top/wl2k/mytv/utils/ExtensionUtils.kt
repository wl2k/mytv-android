package top.wl2k.mytv.utils

const val SECOND = 1000
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Long.humanizeMs() = when (this) {
    in 0..<MINUTE -> "${this / SECOND} 秒"
    in MINUTE..<HOUR -> "${this / MINUTE} 分钟"
    in HOUR..<DAY -> "${this / HOUR} 小时"
    else -> "${this / DAY} 天"
}
