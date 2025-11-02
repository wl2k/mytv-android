package top.wl2k.mytv.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object ApkInstaller {
    @SuppressLint("SetWorldReadable")
    fun installApk(context: Context, file: File) {
        if (file.exists()) {
            // 解决 Android 6 无法解析安装包
            file.setReadable(true, false)

            val uri =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                    context, "${context.packageName}.FileProvider", file
                )
                else Uri.fromFile(file)

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                setDataAndType(uri, "application/vnd.android.package-archive")
            }

            context.startActivity(installIntent)
        }
    }
}