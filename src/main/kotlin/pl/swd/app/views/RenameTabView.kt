package pl.swd.app.views;

import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import tornadofx.*

class RenameTabView : View("Rename Tab") {
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
        model.rebind { tab = tabInput }
        tabNameTextField.requestFocus()
    }

    private fun save() {
        model.commit()
        close()
    }

    class TabViewModel(var tab: Tab) : ViewModel() {
        val tabName = bind { tab.textProperty() }
    }
}
// todo https://github.com/edvin/tornadofx/wiki/ViewModel

