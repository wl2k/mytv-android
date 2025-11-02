package top.wl2k.mytv.data.entities

import androidx.compose.runtime.Immutable
import top.wl2k.mytv.data.entities.Epg.Companion.currentProgrammes

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@Immutable
data class EpgList(
    val value: List<Epg> = emptyList(),
) : List<Epg> by value {
    companion object {
        /**
         * 当前节目/下一个节目
         */
        fun EpgList.currentProgrammes(iptv: Iptv): EpgProgrammeCurrent? {
            return firstOrNull { it.channel == iptv.channelName }?.currentProgrammes()
        }
    }
}