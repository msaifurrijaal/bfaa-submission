package com.learning.githubuser.data.model

import com.google.gson.annotations.SerializedName

data class ResponseSearchUser(
    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val items: List<ResponseItemSearch?>? = null
)
