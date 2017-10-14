package pl.swd.app.views;

import com.github.thomasnield.rxkotlinfx.actionEvents
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import mu.KLogging
import pl.swd.app.exceptions.ProjectDoesNotExistException
import pl.swd.app.models.SpreadSheet
import pl.swd.app.services.DataFileParserService
import pl.swd.app.services.FileIOService
import pl.swd.app.services.ProjectSaverService
import pl.swd.app.services.ProjectService
import pl.swd.app.views.modals.RenameProjectModal
import tornadofx.*
import java.io.File

class MenuBarView : View("My View") {
    companion object : KLogging()

    val projectService: ProjectService by di()
    val projectSaverService: ProjectSaverService by di()
    val fileIOService: FileIOService by di()
    val dataFileParserService: DataFileParserService by di()

    override val root = menubar {
        menu("File") {
            item("Open...", KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)) {
                actionEvents()
                        .subscribe { projectSaverService.loadFromFile(askUserForPath = true) }
            }

            item("Save", KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)) {
                actionEvents()
                        .subscribe { projectSaverService.saveToFile() }
            }

            item("Save As", KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)) {
                actionEvents()
                        .subscribe { projectSaverService.saveToFile(askUserForPath = true) }
            }

            separator()

            item("Import Data", KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN)) {
                actionEvents()
                        .doOnNext { logger.debug { "'Import Data' Dialog clicked" } }
                        .flatMap { fileIOService.openFileDialogObs() }
                        .map(this@MenuBarView::registerSpreadSheet)
                        .subscribe { logger.debug { "Registered new SpreadSheet: ${it}" } }
            }
        }

        menu("Project") {
            item("Rename") {
                actionEvents()
                        .subscribe { projectService.renameProject() }
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
            if (!isPresent()) {
                throw ProjectDoesNotExistException("Cannot register spreadsheet, because Project does not exist")
            }

            val dataTable = dataFileParserService.generateDataTable(file)
            val spreadSheet = SpreadSheet(
                    name = spreadSheetName,
                    dataTable = dataTable
            )

            get().addSpreadSheet(spreadSheet)
        }

        return spreadSheetName
    }
}
