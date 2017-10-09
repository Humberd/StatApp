package pl.swd.app.models;

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class SpreadSheet(name: String,
                  val dataTable: DataTable) {
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty
}
