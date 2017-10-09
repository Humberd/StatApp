package pl.swd.app.views;

import javafx.scene.control.Tab
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import tornadofx.*

class RenameTabModal : Fragment("Rename TabWrapper") {
    companion object : KLogging()

    val tabInput: Tab by param()
    val model = TabViewModel(tabInput)

    override val root = form {
        fieldset("TabWrapper Info") {
            field("TabWrapper Name") {
                textfield(model.tabName).requestFocus()
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
        logger.debug { "Opening a Rename TabWrapper Modal: '${tabInput.text}'" }
    }

    override fun onUndock() {
        logger.debug { "Closing a Rename TabWrapper Modal: '${tabInput.text}'" }
    }

    private fun save() {
        logger.debug { "Changed tab name from: '${tabInput.text}' to: '${model.tabName.value}'" }
        model.commit()
        close()
    }

    class TabViewModel(var tab: Tab) : ViewModel() {
        val tabName = bind { tab.textProperty() }
    }
}
// todo https://github.com/edvin/tornadofx/wiki/ViewModel

