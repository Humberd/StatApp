package pl.swd.app.views;

import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.Tab
import pl.swd.app.models.DataTable
import tornadofx.*
import java.io.File

class AppViewTab(val dataTable: DataTable,
                 val originalFile: File) : Tab() {
    var renameMenuItem: MenuItem by singleAssign()

    init {
        text = originalFile.name

        renameMenuItem = MenuItem("Rename")
        contextMenu = ContextMenu(renameMenuItem)
    }
}
