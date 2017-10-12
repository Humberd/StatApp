package pl.swd.app.models;

import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import pl.swd.app.utils.emptyObservableList
import tornadofx.*

class Project(name: String,
              val spreadSheetList: ObservableList<SpreadSheet> = emptyObservableList()) {
    @Transient
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    fun addSpreadSheet(spreadSheet: SpreadSheet) {
        spreadSheetList.add(spreadSheet)
    }
}