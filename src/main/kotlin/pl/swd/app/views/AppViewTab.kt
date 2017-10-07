package pl.swd.app.views;

import javafx.scene.control.Tab
import pl.swd.app.models.DataTable
import tornadofx.*
import java.io.File

class AppViewTab(val dataTable: DataTable,
                 val originalFile: File) : Tab() {
    init {
        text = originalFile.name

        button("Button")

        contextmenu {
            item("Send Email").action {
                println("Sending Email to")
            }
            item("Change Status").action {
                println("Changing Status for")
            }
        }
    }
}