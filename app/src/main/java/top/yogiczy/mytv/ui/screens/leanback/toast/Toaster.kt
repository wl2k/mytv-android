package top.yogiczy.mytv.ui.screens.leanback.toast

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * 一个用于在应用全局显示 Toast 的单例帮助类。
 */
@SuppressLint("StaticFieldLeak")
class Toaster private constructor(
    private val context: Context,
) {
    private fun show(message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }

    companion object {
        private val instance: Toaster by lazy {
            _context?.let { Toaster(it) }
                ?: throw IllegalStateException(
                    """
                    Toaster has not been initialized.
                    Please call `Toaster.init(this)` in `Application.onCreate()`.
                    """.trimIndent()
                )
        }

        /**
         * show a toast message.
         *
         * @param message 要显示的消息
         * @param duration 显示时长 (默认为 [Toast.LENGTH_SHORT])
         */
        fun show(message: String, duration: Int = Toast.LENGTH_SHORT) {
            instance.show(message, duration)
        }

        @Volatile
        private var _context: Context? = null

        fun init(context: Context) {
            if (_context == null)
                _context = context.applicationContext
        }
    }
}
