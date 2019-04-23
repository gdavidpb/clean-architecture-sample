package com.gdavidpb.test.data.model.api

data class SearchResult<T>(
    val resultCount: Int,
    val results: List<T>
)