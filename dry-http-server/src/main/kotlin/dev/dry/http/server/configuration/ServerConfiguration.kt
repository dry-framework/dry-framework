package dev.dry.http.server.configuration

data class ServerConfiguration(
    val port: Int,
    val contextPath: String,
) {
    companion object {
        private const val DEFAULT_PORT = 9191
        private const val DEFAULT_CONTEXT_PATH = "/api"

        fun default(): ServerConfiguration {
            return ServerConfiguration(
                port = DEFAULT_PORT,
                contextPath = DEFAULT_CONTEXT_PATH,
            )
        }
    }
}
