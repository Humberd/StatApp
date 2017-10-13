package pl.swd.app.views.modals

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import pl.swd.app.models.Project
import tornadofx.*

class UpdateProjectSaveFilePathModal : Modal("Project File") {
    companion object : KLogging()

    val project: Project by param()
    val model = ProjectViewModel(project)

    override val root = form {
        fieldset("Where do you want to save the Project?") {
            field("File Name") {
                textfield(model.saveFileName).requestFocus()
            }
        }

        buttonbar {
            button("Cancel") {
                shortcut(KeyCodeCombination(KeyCode.ESCAPE))
                action {
                    close()
                }
            }

            button("Save") {
                enableWhen(model.dirty)
                shortcut(KeyCodeCombination(KeyCode.ENTER))
                action {
                    save()
                }
            }
        }
    }

    override fun onDock() {
        logger.debug { "Opening an ${this.javaClass.simpleName} with: saveFilePath = '${project.saveFilePath}'" }
        super.onDock()
    }

    override fun onUndock() {
        logger.debug { "Closing an ${this.javaClass.simpleName} with: saveFilePath = '${project.saveFilePath}'" }
        super.onUndock()
    }

    private fun save() {
        logger.debug { "Updated Project saveFilePath from: '${project.saveFilePath}' to: '${model.saveFileName.value}'" }
        model.commit()
        status = ModalStatus.COMPLETED
        close()
    }

    class ProjectViewModel(var project: Project) : ViewModel() {
        val saveFileName = bind { project.saveFilePathProperty }
    }
}