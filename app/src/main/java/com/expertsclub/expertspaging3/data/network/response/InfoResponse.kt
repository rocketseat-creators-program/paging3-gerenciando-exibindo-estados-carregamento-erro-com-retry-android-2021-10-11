package com.expertsclub.expertspaging3.data.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoResponse(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
