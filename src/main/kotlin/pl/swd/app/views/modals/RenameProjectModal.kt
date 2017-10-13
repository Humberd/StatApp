package pl.swd.app.views.modals

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import pl.swd.app.models.Project
import tornadofx.*

class RenameProjectModal : Fragment("Rename Project") {
    companion object : KLogging()

    val project: Project by param()
    val model = ProjectViewModel(project)

    override val root = form {
        fieldset("Project") {
            field("Name") {
                textfield(model.name).requestFocus()
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
        logger.debug { "Opening a Rename Project Modal: '${project.name}'" }
    }

    override fun onUndock() {
        logger.debug { "Closing a Rename Project Modal: '${project.name}'" }
    }

    private fun save() {
        logger.debug { "Changed Project name from: '${project.name}' to: '${model.name.value}'" }
        model.commit()
        close()
    }

    class ProjectViewModel(var project: Project) : ViewModel() {
        val name = bind { project.nameProperty }
    }
}