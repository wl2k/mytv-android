package top.wl2k.mytv.utils

fun Long.humanizeMs() = when (this) {
    in 0..<60_000 -> "${this / 1000}秒"
    in 60_000..<3_600_000 -> "${this / 60_000}分钟"
    else -> "${this / 3_600_000}小时"
}
