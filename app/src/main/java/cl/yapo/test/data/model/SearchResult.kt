package cl.yapo.test.data.model

data class SearchResult<T>(
    val resultCount: Int,
    val results: List<T>
)