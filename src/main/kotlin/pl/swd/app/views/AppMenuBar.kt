package pl.swd.app.views;

import pl.swd.app.controllers.FileIOController
import pl.swd.app.controllers.FileParserController
import tornadofx.*

class AppMenuBar : View("My View") {
    val fileIOController: FileIOController by inject()
    val fileParserController: FileParserController by inject()
    val appTabs: AppTabs by inject()

    override val root = menubar {
        menu("File") {
            item("Open", "Shortcut+O").action {
                val files = fileIOController.openFileDialog()
                files.forEach {
                    val dataTable = fileParserController.generateDataTable(it)
                    val viewTab = AppViewTab(dataTable, it)
                    appTabs.addTab(viewTab)
                }
            }
        }
    }
}
