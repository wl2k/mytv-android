package top.wl2k.mytv

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import top.wl2k.mytv.ui.LeanbackApp
import top.wl2k.mytv.ui.theme.LeanbackTheme
import top.wl2k.mytv.ui.utils.HttpServer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // this would trigger a warning in the manifest, so we've moved it here
        // same as `android:screenOrientation="sensorLandscape"`
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        enableEdgeToEdge()
        setContent {
            // 隐藏状态栏、导航栏
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, window.decorView).let { insetsController ->
                insetsController.hide(WindowInsetsCompat.Type.statusBars())
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            // 屏幕常亮
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            LeanbackTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    LeanbackApp(
                        onBackPressed = {
                            HttpServer.stop()
                            finish()
                        },
                    )
                }
            }
        }

        HttpServer.start(applicationContext)
    }
}
