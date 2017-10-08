package pl.swd.app.views;

import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import tornadofx.*

class RenameTabModal : View("Rename Tab") {
    companion object : KLogging()

    val tabInput: Tab by param()
    val model = TabViewModel(tabInput)

    var tabNameTextField: TextField by singleAssign()

    override val root = form {
        fieldset("Tab Info") {
            field("Tab Name") {
                tabNameTextField = textfield(model.tabName)
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
        logger.debug { "Opening a Rename Tab Modal: '${tabInput.text}'" }

        /*Need to rebind a ViewModel for the new data comming from 'tabInput'*/
        model.rebind { tab = tabInput }
        tabNameTextField.requestFocus()
    }

    override fun onUndock() {
        logger.debug { "Closing a Rename Tab Modal: '${tabInput.text}'" }
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

