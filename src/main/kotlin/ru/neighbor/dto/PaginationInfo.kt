package ru.neighbor.dto

data class PaginationInfo<TItems>(
    val page: Int = 0,
    val pageSize: Int = 0,
    val itemsCount: Int = 0,
    val pageCount: Int = 0,
    val items: List<TItems>
)
