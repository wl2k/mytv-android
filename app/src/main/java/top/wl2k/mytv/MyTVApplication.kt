package top.wl2k.mytv

import android.app.Application
import top.wl2k.mytv.ui.utils.SP

class MyTVApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        UnsafeTrustManager.enable()
        AppGlobal.cacheDir = applicationContext.cacheDir
        SP.init(applicationContext)
    }
}
