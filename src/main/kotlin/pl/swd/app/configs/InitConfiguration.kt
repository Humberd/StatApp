package pl.swd.app.configs;

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import pl.swd.app.exceptions.ConfigDoesNotExistException
import pl.swd.app.exceptions.ValueNotInitializedException
import pl.swd.app.services.ConfigSaverService
import pl.swd.app.services.ProjectSaverService
import java.io.FileNotFoundException
import javax.annotation.PostConstruct

@Configuration
open class InitConfiguration {
    companion object : KLogging()

    @Autowired lateinit var configSaverService: ConfigSaverService
    @Autowired lateinit var projectSaverService: ProjectSaverService

    init {
        logger.debug { "Initializing Stat App's Config" }

    }

    @PostConstruct
    private fun init() {
        /*First we need to load a config, because app depends on it*/
        try {
            configSaverService.loadFromFile()
        } catch (ex: FileNotFoundException) {
            configSaverService.loadDefaultConfig()
            configSaverService.saveToFile()
        }

        /*Then we try to load a project from file*/
        try {
            projectSaverService.loadFromFile()
        } catch (ex: Exception) {
            when (ex) {
                is ValueNotInitializedException,
                is ConfigDoesNotExistException -> {
                    projectSaverService.loadDefaultProject()
                }
                else -> throw ex
            }
        }
    }
}