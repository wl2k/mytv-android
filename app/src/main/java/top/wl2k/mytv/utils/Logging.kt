package top.wl2k.mytv.utils

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.serialization.Serializable
import top.wl2k.mytv.data.utils.Constants

/**
 * 日志工具类
 */
class Logging(
    private val tag: String
) {
    fun d(message: String, throwable: Throwable? = null) {
        Log.d(tag, message, throwable)
        // addHistoryItem(HistoryItem(LevelType.DEBUG, tag, message, throwable?.message))
    }

    fun i(message: String, throwable: Throwable? = null) {
        Log.i(tag, message, throwable)
        addHistoryItem(HistoryItem(LevelType.INFO, tag, message, throwable?.message))
    }

    fun w(message: String, throwable: Throwable? = null) {
        Log.w(tag, message, throwable)
        addHistoryItem(HistoryItem(LevelType.WARN, tag, message, throwable?.message))
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
        addHistoryItem(HistoryItem(LevelType.ERROR, tag, message, throwable?.message))
    }

    companion object {
        private val _history = mutableListOf<HistoryItem>()
        val history: List<HistoryItem>
            get() = _history

        fun addHistoryItem(item: HistoryItem) {
            _history.add(item)
            if (_history.size > Constants.LOG_HISTORY_MAX_SIZE) _history.removeAt(0)
        }
    }

    enum class LevelType {
        INFO, WARN, ERROR
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Serializable
    data class HistoryItem(
        val level: LevelType,
        val tag: String,
        val message: String,
        val cause: String? = null,
        val time: Long = System.currentTimeMillis(),
    )
}

/**
 * 一个标记接口，用于：
 * 1. 告诉 ProGuard/R8 不要混淆实现此接口的类的名称。
 * 2. 自动为实现类提供一个 Logging 实例。
 */
interface Logger {
    /**
     * 提供一个 Logging 实例。
     * 'javaClass.simpleName' 会在运行时获取到具体实现类的名称。
     */
    val log: Logging
        get() = Logging(javaClass.simpleName)
}
