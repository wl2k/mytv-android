package top.wl2k.mytv

import android.app.Application
import top.wl2k.mytv.ui.screens.leanback.toast.Toaster
import top.wl2k.mytv.ui.utils.SP

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        UnsafeTrustManager.enable()
        AppGlobal.cacheDir = applicationContext.cacheDir
        Toaster.init(applicationContext)
        SP.init(applicationContext)
    }
}
