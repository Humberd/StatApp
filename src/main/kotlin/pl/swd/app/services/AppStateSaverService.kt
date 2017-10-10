package pl.swd.app.services;

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.swd.app.models.Project

@Service
open class AppStateSaverService {
    @Autowired lateinit var fileIOService: FileIOService

    fun saveAppState(project: Project, fileName: String = project.name) {
        fileIOService.saveAsJsonToFile(project, fileName)
    }

    fun retrieveAppState(fileName: String): Project {
        return fileIOService.getAsObjectFromJsonFile(fileName)
    }
}