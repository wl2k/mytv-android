@file:OptIn(InternalSerializationApi::class)

package top.wl2k.mytv.data.entities

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import top.wl2k.mytv.data.entities.EpgProgramme.Companion.isLive

/**
 * 频道节目单
 */
@Serializable
data class Epg(
    /**
     * 频道名称
     */
    val channel: String = "",

    /**
     * 节目列表
     */
    val programmes: EpgProgrammeList = EpgProgrammeList(),
) {
    companion object {
        /**
         * 当前节目/下一个节目
         */
        fun Epg.currentProgrammes(): EpgProgrammeCurrent? {
            val currentProgramme = programmes.firstOrNull { it.isLive() } ?: return null

            return EpgProgrammeCurrent(
                now = currentProgramme,
                next = programmes.indexOf(currentProgramme).let { index ->
                    if (index + 1 < programmes.size) programmes[index + 1]
                    else null
                },
            )
        }
    }
}