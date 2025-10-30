package top.yogiczy.mytv.data.entities

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@SuppressLint("UnsafeOptInUsageError")
@Serializable
@Immutable
data class EpgProgrammeList(
    val value: List<EpgProgramme> = emptyList(),
) : List<EpgProgramme> by value
