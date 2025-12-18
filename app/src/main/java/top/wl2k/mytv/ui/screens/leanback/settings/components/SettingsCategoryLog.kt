package top.wl2k.mytv.ui.screens.leanback.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import top.wl2k.mytv.ui.theme.LeanbackTheme
import top.wl2k.mytv.utils.Logging
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LeanbackSettingsCategoryLog(
    modifier: Modifier = Modifier,
    history: List<Logging.HistoryItem> = emptyList(),
) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val historySorted = remember(history) {
        history.sortedByDescending { it.time }
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
    ) {
        items(historySorted) {
            LeanbackSettingsCategoryListItem(
                headlineContent = "${it.level.toString()[0]} ${it.tag}",
                supportingContent = it.message,
                trailingContent = timeFormat.format(it.time),
            )
        }
    }
}

@Preview
@Composable
private fun LeanbackSettingsCategoryLogPreview() {
    LeanbackTheme {
        LeanbackSettingsCategoryLog(
            history = persistentListOf(
                Logging.HistoryItem(
                    level = Logging.LevelType.ERROR,
                    tag = "LeanbackSettingsCategoryLog",
                    message = "This is a test message",
                    time = System.currentTimeMillis(),
                ),
            )
        )
    }
}
