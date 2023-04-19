package dev.dry.data.pagination

class Page<T>(
    val content: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalSize: Int
) {
    companion object {
        fun <T> from(
            items: List<T>,
            pageRequest: PageRequest
        ): Page<T> {
            val totalSize = items.size
            val totalPages = totalSize / pageRequest.pageSize +
                    if (totalSize % pageRequest.pageSize > 0) 1 else 0
            val offset = (pageRequest.page - 1) * pageRequest.pageSize
            val content = if (offset >= items.size) {
                emptyList()
            } else {
                val toIndex = if (offset + pageRequest.pageSize < items.size) {
                    offset + pageRequest.pageSize
                } else items.size
                items.subList(offset, toIndex)
            }

            return Page(
                content = content,
                page = pageRequest.page,
                pageSize = pageRequest.pageSize,
                totalPages = totalPages,
                totalSize = totalSize,
            )
        }
    }
}
