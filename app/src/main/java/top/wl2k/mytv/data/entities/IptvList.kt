package top.wl2k.mytv.data.entities

import androidx.compose.runtime.Immutable

/**
 * 直播源列表
 */
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@Immutable
data class IptvList(
    val value: List<Iptv> = emptyList(),
) : List<Iptv> by value {
    companion object {
        val EXAMPLE = IptvList(List(10) { i -> Iptv.EXAMPLE.copy(name = "CCTV-$i") })
    }
}
