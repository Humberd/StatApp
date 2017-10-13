package pl.swd.app.models

/**
 * Stores data from a text config file existing during app lifespan
 */
data class Config(
        var lastOpenedProjectFileName: String? = null
)