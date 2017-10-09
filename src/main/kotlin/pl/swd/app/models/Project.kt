package pl.swd.app.models;

import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import pl.swd.app.utils.emptyObservableList
import tornadofx.*

class Project(name: String,
              val spreadSheetList: ObservableList<SpreadSheet> = emptyObservableList()) {
    val nameProperty = SimpleStringProperty(name)
    val name by nameProperty

    fun addSpreadSheet(spreadSheet: SpreadSheet) {
        spreadSheetList.add(spreadSheet)
    }
}