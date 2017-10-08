package pl.swd.app.views;

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.doOnNextFx
import com.github.thomasnield.rxkotlinfx.events
import io.reactivex.Observable
import io.reactivex.rxjavafx.observables.JavaFxObservable
import javafx.event.ActionEvent
import javafx.event.EventType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import mu.KLogging
import pl.swd.app.controllers.FileIOController
import pl.swd.app.controllers.FileParserController
import tornadofx.*
import java.io.File

class AppMenuBar : View("My View") {
    companion object : KLogging()

    val fileIOController: FileIOController by inject()
    val fileParserController: FileParserController by inject()
    val appTabs: AppTabs by inject()

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
                        .map(this@AppMenuBar::registerTab)
                        .subscribe { logger.debug { "Registered new tab: ${it.text}" } }
            }
        }
    }

    fun registerTab(file: File): AppViewTab {
        val dataTable = fileParserController.generateDataTable(file)
        val viewTab = AppViewTab(dataTable, file)
        appTabs.addTab(viewTab)

        return viewTab
    }
}
