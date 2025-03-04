package com.jop.jetpack.firebase.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataNotification(
    @SerialName("title")
    val title: String = "",
    @SerialName("body")
    val body: String = "",
    @SerialName("target_value")
    val targetValue: String = "",
    @SerialName("target_data")
    val targetData: String = ""
)


