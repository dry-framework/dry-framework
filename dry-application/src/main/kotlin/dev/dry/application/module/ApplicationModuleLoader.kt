package dev.dry.application.module


interface ApplicationModuleLoader {
    fun load(): Collection<ApplicationModule>
}
