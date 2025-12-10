package top.wl2k.mytv.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import top.wl2k.mytv.activities.LeanbackActivity
import top.wl2k.mytv.ui.utils.SP

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && SP.appBootLaunch) {
            val launchIntent = Intent(context, LeanbackActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(launchIntent)
        }
    }
}
