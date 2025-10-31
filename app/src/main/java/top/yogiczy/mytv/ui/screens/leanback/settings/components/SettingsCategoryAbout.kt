package top.yogiczy.mytv.ui.screens.leanback.settings.components

import android.content.Context
import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import top.yogiczy.mytv.data.utils.Constants
import top.yogiczy.mytv.ui.screens.leanback.components.LeanbackQrcode
import top.yogiczy.mytv.ui.theme.LeanbackTheme
import top.yogiczy.mytv.ui.utils.HttpServer

@Composable
fun LeanbackSettingsCategoryAbout(
    modifier: Modifier = Modifier,
    packageInfo: PackageInfo = rememberPackageInfo(),
    serverUrl: String = HttpServer.serverUrl,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
    ) {
        item {
            LeanbackSettingsCategoryListItem(
                headlineContent = "版本",
                trailingContent = packageInfo.versionName!!,
            )
        }
        item {
            LeanbackSettingsCategoryListItem(
                headlineContent = "GitHub 仓库",
                trailingContent = Constants.APP_REPO
            )
        }
        item {
            LeanbackSettingsCategoryListItem(
                headlineContent = "HTTP",
                trailingContent = serverUrl,
            )
        }
        item {
            var show by remember {
                mutableStateOf(
                    false
                )
            }

            LaunchedEffect(Unit) {
                delay(100)
                show = true
            }

            if (show) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LeanbackQrcode(
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp),
                        text = serverUrl,
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberPackageInfo(context: Context = LocalContext.current): PackageInfo =
    context.packageManager.getPackageInfo(context.packageName, 0)


@Preview
@Composable
private fun LeanbackSettingsAboutPreview() {
    LeanbackTheme {
        LeanbackSettingsCategoryAbout(
            packageInfo = PackageInfo().apply { versionName = "1.0.0" },
            serverUrl = "http://127.0.0.1:10481",
        )
    }
}
