package com.shivaay20005.innertube.models.body

import com.shivaay20005.innertube.models.Context
import com.shivaay20005.innertube.models.Continuation
import kotlinx.serialization.Serializable

@Serializable
data class BrowseBody(
    val context: Context,
    val browseId: String?,
    val params: String?,
    val continuation: String?
)
