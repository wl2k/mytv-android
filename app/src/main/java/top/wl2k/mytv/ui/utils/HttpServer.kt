package top.wl2k.mytv.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.RawRes
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.SOCKET_READ_TIMEOUT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import top.wl2k.mytv.R
import top.wl2k.mytv.data.repositories.epg.EpgRepository
import top.wl2k.mytv.data.repositories.iptv.IptvRepository
import top.wl2k.mytv.data.utils.Constants
import top.wl2k.mytv.ui.screens.leanback.toast.Toaster
import top.wl2k.mytv.utils.ApkInstaller
import top.wl2k.mytv.utils.Loggable
import top.wl2k.mytv.utils.Logger
import java.io.File
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

object HttpServer : Loggable() {
    private const val SERVER_PORT = 10481

    @SuppressLint("StaticFieldLeak")
    private var server: TvHttpServer? = null

    val serverUrl: String by lazy { "http://${getLocalIpAddress()}:${SERVER_PORT}" }

    fun start(context: Context) {
        try {
            server = TvHttpServer(context).apply { start(SOCKET_READ_TIMEOUT, false) }
            log.i("服务已启动于: $serverUrl")
        } catch (ex: IOException) {
            log.e("服务启动失败: ${ex.message}", ex)
            CoroutineScope(Dispatchers.Main).launch {
                Toaster.show("HTTP Server 启动失败")
            }
        }
    }

    private fun getLocalIpAddress(): String {
        val defaultIp = "127.0.0.1"
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress ?: defaultIp
                    }
                }
            }
        } catch (ex: SocketException) {
            log.e("获取IP地址失败: ${ex.message}", ex)
        }
        return defaultIp
    }

    private class TvHttpServer(private val context: Context) : NanoHTTPD(SERVER_PORT) {
        private val json = Json { ignoreUnknownKeys = true }

        override fun serve(session: IHTTPSession): Response = runBlocking {
            val router = Router(session, context, json)
            try {
                withContext(Dispatchers.IO) {
                    when {
                        // 静态资源
                        router.matches(Method.GET, "/") -> router.handleIndex()
                        router.matches(Method.GET, "/css") -> router.handleCss()
                        router.matches(Method.GET, "/js") -> router.handleJs()

                        // API
                        router.matches(Method.GET, "/api/settings") -> router.handleGetSettings()
                        router.matches(Method.PUT, "/api/settings") -> router.handlePutSettings()
                        router.matches(Method.POST, "/api/upload/apk") -> router.handleUploadApk()

                        // 404 Not Found
                        else -> newErrorResponse("Not Found", 2, Response.Status.NOT_FOUND)
                    }
                }
            } catch (ex: Exception) {
                log.e("处理请求失败: ${session.uri}, 错误: ${ex.message}", ex)
                newErrorResponse()
            }
        }

        private inner class Router(
            private val session: IHTTPSession,
            private val context: Context,
            private val json: Json,
        ) {
            fun matches(method: Method, uri: String) =
                session.method == method && session.uri.equals(uri)

            fun handleIndex() = newRawResponse(context, "text/html", R.raw.index)
            fun handleCss() = newRawResponse(context, "text/css", R.raw.styles)
            fun handleJs() = newRawResponse(context, "text/javascript", R.raw.script)

            fun handleGetSettings(): Response {
                val settings = AllSettings(
                    appRepo = Constants.APP_REPO,
                    iptvSourceUrl = SP.iptvSourceUrl,
                    epgXmlUrl = SP.epgXmlUrl,
                    videoPlayerUserAgent = SP.videoPlayerUserAgent,
                    logHistory = Logger.history,
                )
                return newJsonResponse(settings)
            }

            suspend fun handlePutSettings(): Response {
                val body = parseBody(session)
                val newSettings = json.decodeFromString<AllSettings>(body)

                SP.iptvSourceUrl = newSettings.iptvSourceUrl.also {
                    if (it != SP.iptvSourceUrl) IptvRepository().clearCache()
                }
                SP.epgXmlUrl = newSettings.epgXmlUrl.also {
                    if (it != SP.epgXmlUrl) EpgRepository().clearCache()
                }
                SP.videoPlayerUserAgent = newSettings.videoPlayerUserAgent

                return newSuccessResponse()
            }

            suspend fun handleUploadApk(): Response {
                val pathMap = mutableMapOf<String, String>()
                try {
                    session.parseBody(pathMap)
                    val apkPath =
                        pathMap["file"] ?: return newErrorResponse(
                            "unable to read APK",
                            code = 4,
                            statusCode = Response.Status.BAD_REQUEST
                        )

                    withContext(Dispatchers.Main) {
                        Toaster.show("apk: 准备安装...")
                    }

                    // An APK file must have the extension ".apk"
                    val sourceFile = File(apkPath)
                    val targetFile = File("$apkPath.apk").apply {
                        deleteOnExit()
                    }
                    sourceFile.renameTo(targetFile)

                    ApkInstaller.installApk(context, targetFile)
                    return newSuccessResponse()
                } catch (e: Exception) {
                    log.e("处理 APK 失败", e)
                    return newErrorResponse("unable to process APK: ${e.message}", code = 5)
                }
            }

            private suspend fun parseBody(session: IHTTPSession): String {
                return withContext(Dispatchers.IO) {
                    val bodySize = session.headers["content-length"]?.toIntOrNull() ?: 0
                    if (bodySize == 0) return@withContext ""

                    val buffer = ByteArray(bodySize)
                    session.inputStream.read(buffer, 0, bodySize)
                    String(buffer)
                }
            }
        }

        // --- 响应辅助函数 ---
        private fun newResponse(
            status: Response.Status,
            data: String,
            mimeType: String = "application/json",
        ) = newFixedLengthResponse(status, mimeType, data)

        private inline fun <reified T> newJsonResponse(
            data: T,
            code: Response.Status = Response.Status.OK
        ): Response {
            val jsonString = json.encodeToString(data)
            return newResponse(code, jsonString)
        }

        @Serializable
        data class Result(val code: Int, val message: String)

        private fun newSuccessResponse() =
            newJsonResponse(Result(0, "success"))

        private fun newErrorResponse(
            message: String = "Internal Server Error",
            code: Int = 1,
            statusCode: Response.Status = Response.Status.INTERNAL_ERROR
        ) =
            newJsonResponse(Result(code, message), statusCode)

        private fun newRawResponse(
            context: Context,
            mimeType: String,
            @RawRes resourceId: Int,
        ): Response = try {
            val inputStream = context.resources.openRawResource(resourceId)
            newFixedLengthResponse(
                Response.Status.OK,
                mimeType,
                inputStream,
                inputStream.available().toLong()
            )
        } catch (e: Exception) {
            log.e("读取资源失败: $resourceId", e)
            newErrorResponse(
                "unable to read raw: $resourceId",
                code = 3
            )
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
private data class AllSettings(
    val appRepo: String,
    val iptvSourceUrl: String,
    val epgXmlUrl: String,
    val videoPlayerUserAgent: String,
    val logHistory: List<Logger.HistoryItem>,
)
