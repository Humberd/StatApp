package pl.swd.app.views;

import com.github.thomasnield.rxkotlinfx.actionEvents
import io.reactivex.Observable
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import mu.KLogging
import pl.swd.app.services.FileIOService
import pl.swd.app.services.DataFileParserService
import pl.swd.app.exceptions.ProjectDoesNotExistException
import pl.swd.app.models.SpreadSheet
import pl.swd.app.services.ProjectService
import tornadofx.*
import java.io.File
import java.util.*

class MenuBarView : View("My View") {
    companion object : KLogging()

    val projectService: ProjectService by di()
    val fileIOService: FileIOService by di()
    val dataFileParserService: DataFileParserService by di()

    override val root = menubar {
        menu("File") {
            item("Open Project", KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)) {

            }

            item("Save Project", KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)) {

            }


            separator()
            item("Import Data", KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN)) {
                actionEvents()
                        .doOnNext { logger.debug { "'Open File' Dialog clicked" } }
                        .flatMap { fileIOService.openFileDialog() }
                        .map { file ->
                            val optionsView  = find<ParseFileOptionModalView>().apply { openModal(block = true) }

                            if (optionsView.cancelFlag) {
                                return@map
                            }

                            registerSpreadSheet(Pair(file,optionsView.getResultList().get()))
                        }
                        .subscribe { logger.debug { "Registered new SpreadSheet: ${it}" } }
            }
        }
    }

    /**
     * Registers new SpreadSheet to currentProject
     *
     * @return spreadSheet name
     */
    fun registerSpreadSheet(pair: Pair<File, List<String>>): String {
        val spreadSheetName = pair.first.name

        projectService.currentProject.value.apply {
            /*Can only register a spreadsheet when currentProject exists*/
            if (!isPresent()) {
                throw ProjectDoesNotExistException("Cannot register spreadsheet, because Project does not exist")
            }

            val dataTable = dataFileParserService.generateDataTable(pair)
            val spreadSheet = SpreadSheet(
                    name = spreadSheetName,
                    dataTable = dataTable
            )

            get().addSpreadSheet(spreadSheet)
        }

        return spreadSheetName
    }
}