package pl.swd.app.views;

import com.github.thomasnield.rxkotlinfx.actionEvents
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import mu.KLogging
import pl.swd.app.controllers.FileIOController
import pl.swd.app.controllers.FileParserController
import pl.swd.app.exceptions.ProjectDoesNotExistException
import pl.swd.app.models.SpreadSheet
import pl.swd.app.services.ProjectService
import tornadofx.*
import java.io.File

class MenuBarView : View("My View") {
    companion object : KLogging()

    val projectService: ProjectService by di()
    val fileIOController: FileIOController by inject()
    val fileParserController: FileParserController by inject()

    init {
        // todo only for debugging purposes, remove this file
        registerSpreadSheet(File(fileIOController.getCurrentDirectory() + "/testFile.txt"))
    }

    override val root = menubar {
        menu("File") {
            item("Open", KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)) {
                actionEvents()
                        .doOnNext { logger.debug { "'Open File' Dialog clicked" } }
                        .flatMap { fileIOController.openFileDialog() }
                        .map(this@MenuBarView::registerSpreadSheet)
                        .subscribe { logger.debug { "Registered new SpreadSheet: ${it}" } }
            }
        }
    }

    /**
     * Registers new SpreadSheet to currentProject
     *
     * @return spreadSheet name
     */
    fun registerSpreadSheet(file: File): String {
        val spreadSheetName = file.name

        projectService.currentProject.value.apply {
            /*Can only register a spreadsheet when currentProject exists*/
            if (!isPresent) {
                throw ProjectDoesNotExistException()
            }

            val dataTable = fileParserController.generateDataTable(file)
            val spreadSheet = SpreadSheet(
                    name = spreadSheetName,
                    dataTable = dataTable
            )

            get().addSpreadSheet(spreadSheet)
        }

        return spreadSheetName
    }
}
