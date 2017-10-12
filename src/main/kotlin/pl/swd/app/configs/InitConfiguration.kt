package pl.swd.app.configs;

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import pl.swd.app.exceptions.ValueNotInitializedException
import pl.swd.app.services.ConfigSaverService
import pl.swd.app.services.ProjectSaverService
import java.io.FileNotFoundException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
open class InitConfiguration {
    companion object : KLogging()

    @Autowired lateinit var configSaverService: ConfigSaverService
    @Autowired lateinit var projectSaverService: ProjectSaverService

    @PostConstruct
    private fun init() {
        logger.info { "Initializing Stat App" }
        this.loadConfig()
        this.loadProject()
    }

    @PreDestroy
    private fun destroy() {
        logger.info { "Shutting down Stat App" }
        // todo check why not saving
        configSaverService.saveToFile()
    }

    private fun loadConfig() {
        try {
            configSaverService.loadFromFile()
        } catch (ex: FileNotFoundException) {
            logger.debug { ex.message }
            configSaverService.loadDefaultConfig()
            configSaverService.saveToFile()
        }
    }

    private fun loadProject() {
        try {
            projectSaverService.loadFromFile()
        } catch (ex: ValueNotInitializedException) {
            logger.debug { ex.message }
            projectSaverService.loadDefaultProject()
        }
    }
}