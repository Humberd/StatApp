package pl.swd.app.configs;

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import pl.swd.app.services.AppStateSaverService
import pl.swd.app.services.ProjectService

@Configuration
open class InitConfiguration {
    companion object: KLogging()

    @Autowired lateinit var projectService: ProjectService
    @Autowired lateinit var appStateSaverService: AppStateSaverService

    init {
        logger.debug { "Initializing Stat App's Config" }


    }

//    private fun save
}