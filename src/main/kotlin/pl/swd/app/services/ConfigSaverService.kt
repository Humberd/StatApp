package pl.swd.app.services;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.swd.app.exceptions.ConfigDoesNotExistException
import pl.swd.app.interfaces.Savable
import pl.swd.app.models.Config

@Service
class ConfigSaverService : Savable {
    @Autowired private lateinit var configService: ConfigService
    @Autowired private lateinit var fileIOService: FileIOService
    @Autowired private lateinit var applicationPropertiesService: ApplicationPropertiesService

    /**
     * Saves application configs to a file with a name provided in 'application.properties'
     */
    override fun saveToFile() {
        val config = configService.currentConfig.value.apply {
            if (this == null) {
                throw ConfigDoesNotExistException("Cannot save Config to file, because Config does not exist")
            }
        }

        fileIOService.saveAsJsonToFile(
                data = config,
                fileName = applicationPropertiesService.configFileName
        )
    }

    /**
     * Loads application configs from a file with a name provided in 'application.properties'
     */
    override fun loadFromFile() {
        val config = fileIOService.getAsObjectFromJsonFile<Config>(applicationPropertiesService.configFileName)

        configService.setCurrentConfig(config)
    }

    fun loadDefaultConfig() {
        configService.setCurrentConfig(Config())
    }
}