package pl.swd.app.services;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.swd.app.exceptions.ConfigDoesNotExistException
import pl.swd.app.exceptions.ProjectDoesNotExistException
import pl.swd.app.exceptions.ValueNotInitializedException
import pl.swd.app.interfaces.Savable
import pl.swd.app.models.Project
import java.io.FileNotFoundException

@Service
open class ProjectSaverService : Savable {
    @Autowired private lateinit var fileIOService: FileIOService
    @Autowired private lateinit var projectService: ProjectService
    @Autowired private lateinit var applicationPropertiesService: ApplicationPropertiesService
    @Autowired private lateinit var configService: ConfigService


    /**
     * Saves current project to a file with a name of project name
     */
    override fun saveToFile() {
        val project = projectService.currentProject.value.apply {
            if (!isPresent()) {
                throw ProjectDoesNotExistException("Cannot save app state, because a project does not exist")
            }
        }.get()

        fileIOService.saveAsJsonToFile(
                data = project,
                fileName = getProjectNameWithExtension(project.name))
    }

    /**
     * Loads a project from a file with a name saved in a live configs file
     */
    override fun loadFromFile() {
        val lastOpenedProjectName = configService.currentConfig.value.apply {
            if (this == null) {
                throw ConfigDoesNotExistException("Cannot load project from file, because Config does not exist")
            }
        }.lastOpenedProjectName.apply {
            if (this == null) {
                throw ValueNotInitializedException("Value 'lastOpenedProjectName' is not set in a Config. Aborting loading project from a file")
            }
        }!! // i force anti null, because I already validated it above

        val project = fileIOService.getAsObjectFromJsonFile<Project>(getProjectNameWithExtension(lastOpenedProjectName))

        projectService.setCurrentProject(project)
    }

    private fun getProjectNameWithExtension(projectName: String): String {
        return projectName + "." + applicationPropertiesService.projectFileExtension
    }

    fun loadDefaultProject() {
        projectService.setCurrentProject(Project("Empty Project"))
    }
}