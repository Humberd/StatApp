package pl.swd.app.views;

import javafx.scene.control.Tab
import javafx.scene.control.TextField
import tornadofx.*

class RenameTabView : View("Rename tabInput") {
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
            button("Reset").action {
                model.rollback()
            }

            button("Save") {
                enableWhen(model.dirty)
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
}
// todo https://github.com/edvin/tornadofx/wiki/ViewModel

class TabViewModel(var tab: Tab) : ViewModel() {
    val tabName = bind { tab.textProperty() }
}




