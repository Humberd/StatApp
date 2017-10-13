package pl.swd.app.views.modals

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import pl.swd.app.models.Project
import tornadofx.*

class RenameProjectModal : Fragment("Rename Project") {
    companion object : KLogging()

    val project: Project by param()
    val model = PorjectViewModel(project)

    override val root = form {
        fieldset("SpreadSheet") {
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
        logger.debug { "Opening a Rename SpreadSheet Modal: '${project.name}'" }
    }

    override fun onUndock() {
        logger.debug { "Closing a Rename SpreadSheet Modal: '${project.name}'" }
    }

    private fun save() {
        logger.debug { "Changed SpreadSheet name from: '${project.name}' to: '${model.name.value}'" }
        model.commit()
        close()
    }

    class PorjectViewModel(var project: Project) : ViewModel() {
        val name = bind { project.nameProperty }
    }
}