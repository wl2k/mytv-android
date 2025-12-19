package top.wl2k.mytv.utils

import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long.humanizeMs() = "${toDuration(DurationUnit.MILLISECONDS)}"
