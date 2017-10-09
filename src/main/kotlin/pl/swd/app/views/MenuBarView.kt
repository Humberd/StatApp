package pl.swd.app.views;

import com.github.thomasnield.rxkotlinfx.actionEvents
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import mu.KLogging
import pl.swd.app.controllers.FileIOController
import pl.swd.app.controllers.FileParserController
import tornadofx.*
import java.io.File

class MenuBarView : View("My View") {
    companion object : KLogging()

    val fileIOController: FileIOController by inject()
    val fileParserController: FileParserController by inject()
    val tabsView: TabsView by inject()

    init {
        // todo only for debugging purposes, remove this file
        registerTab(File(fileIOController.getCurrentDirectory() + "/testFile.txt"))
    }


    override val root = menubar {
        menu("File") {
            item("Open", KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)) {

                actionEvents()
                        .doOnNext { logger.debug { "'Open File' Dialog clicked" } }
                        .flatMap { fileIOController.openFileDialog() }
                        .map(this@MenuBarView::registerTab)
                        .subscribe { logger.debug { "Registered new tab: ${it.text}" } }
            }
        }
    }

    fun registerTab(file: File): Tab {
        val dataTable = fileParserController.generateDataTable(file)
        val viewTab = Tab(dataTable, file)
        tabsView.addTab(viewTab)

        return viewTab
    }
}
