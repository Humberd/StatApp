package pl.swd.app.views;

import pl.swd.app.controllers.FileIOController
import pl.swd.app.controllers.FileParserController
import tornadofx.*
import java.io.File

class AppMenuBar : View("My View") {
    val fileIOController: FileIOController by inject()
    val fileParserController: FileParserController by inject()
    val appTabs: AppTabs by inject()

    init {
        // todo only for debugging purposes, remove this file
        registerTab(File(fileIOController.getCurrentDirectory() + "/testFile.txt"))
    }


    override val root = menubar {
        menu("File") {
            item("Open", "Shortcut+O").action {
                val files = fileIOController.openFileDialog()
                files.forEach { registerTab(it) }
            }
        }
    }

    fun registerTab(file: File) {
        val dataTable = fileParserController.generateDataTable(file)
        val viewTab = AppViewTab(dataTable, file)
        appTabs.addTab(viewTab)
    }
}
