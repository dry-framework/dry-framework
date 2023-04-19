package dev.dry.application.module

import java.util.*


object DefaultApplicationModuleLoader: ApplicationModuleLoader {
    override fun load(): Collection<ApplicationModule> {
        return ServiceLoader
            .load(ApplicationModuleProvider::class.java)
            .map(ApplicationModuleProvider::provide)
    }
}
