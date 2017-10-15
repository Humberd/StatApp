package pl.swd.app.views.modals

import javafx.scene.Parent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import pl.swd.app.models.DataColumn
import pl.swd.app.models.DataValue
import pl.swd.app.models.Project
import pl.swd.app.models.SpreadSheet
import tornadofx.*

class ConvertValuesModal : Modal("Convert table data") {
    companion object : KLogging()

    val columnNameList: ArrayList<String> by param()
    val comboBox = combobox<String>()

    override val root = borderpane {
        center {
            hbox {
                button("Save") {
                    shortcut(KeyCodeCombination(KeyCode.ENTER))
                    action {
                        save()
                    }
                }
            }.add(comboBox)
        }
    }

    init {
        comboBox.items.addAll(columnNameList)
    }

    private fun save() {
        if (comboBox.selectionModel.selectedItem.isNullOrEmpty()) {
            close()
        } else {
            status = ModalStatus.COMPLETED
            close()
        }
    }

    fun getSelectedValue(): String {
        return comboBox.selectionModel.selectedItem
    }
}