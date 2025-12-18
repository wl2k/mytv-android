package top.wl2k.mytv.ui.screens.leanback.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.wl2k.mytv.ui.screens.leanback.settings.LeanbackSettingsCategories
import top.wl2k.mytv.utils.Logging

@Composable
fun LeanbackSettingsCategoryContent(
    modifier: Modifier = Modifier,
    focusedCategoryProvider: () -> LeanbackSettingsCategories = { LeanbackSettingsCategories.entries.first() },
) {
    val focusedCategory = focusedCategoryProvider()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = focusedCategory.title, style = MaterialTheme.typography.headlineSmall)

        when (focusedCategory) {
            LeanbackSettingsCategories.IPTV -> LeanbackSettingsCategoryIptv()
            LeanbackSettingsCategories.EPG -> LeanbackSettingsCategoryEpg()
            LeanbackSettingsCategories.UI -> LeanbackSettingsCategoryUI()
            LeanbackSettingsCategories.FAVORITE -> LeanbackSettingsCategoryFavorite()
            LeanbackSettingsCategories.VIDEO_PLAYER -> LeanbackSettingsCategoryVideoPlayer()
            LeanbackSettingsCategories.HTTP -> LeanbackSettingsCategoryHttp()
            LeanbackSettingsCategories.DEBUG -> LeanbackSettingsCategoryDebug()
            LeanbackSettingsCategories.LOG -> LeanbackSettingsCategoryLog(
                history = Logging.history,
            )

            LeanbackSettingsCategories.ABOUT -> LeanbackSettingsCategoryAbout()
        }
    }
}