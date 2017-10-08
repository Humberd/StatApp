package pl.swd.app.views;

import javafx.scene.control.Tab
import javafx.scene.control.TextField
import tornadofx.*

class RenameTabView : View("Rename tab") {
    val tab: Tab by param()
    lateinit var model: TabViewModel

    var tabNameTextField: TextField by singleAssign()

    init {
        println("inited")
    }

    override val root = form {
        fieldset("Tab Info") {
            field("Tab Name") {
                tabNameTextField = textfield()
            }
        }
        button("Commit") {
            action { save() }
        }
    }

    override fun onDock() {
        model = TabViewModel(tab)

        with(tabNameTextField) {
            bind(model.tabName)
            requestFocus()
        }
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




