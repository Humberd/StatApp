package pl.swd.app.views.modals

import com.github.thomasnield.rxkotlinfx.toObservable
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import mu.KLogging
import pl.swd.app.models.DataColumn
import pl.swd.app.models.SpreadSheet
import pl.swd.app.services.ProjectService
import pl.swd.app.utils.emptyObservableList
import tornadofx.*

class Chart2DConfigModal : Modal("2D Chart") {
    companion object : KLogging()

    val projectService: ProjectService by di()
    val selectedSpreadSheet = SimpleObjectProperty<SpreadSheet>()
    val model = Chart2DConfigViewModel()

    override val root = form {
        fieldset("Choose a SpreadSheet to build a chart from") {
            field("SpreadSheet") {
                combobox<SpreadSheet>(property = selectedSpreadSheet) {
                    projectService.currentProject
                            .map {
                                if (!it.isPresent()) {
                                    return@map emptyObservableList<SpreadSheet>()
                                }

                                return@map it.get().spreadSheetList
                            }
                            .doOnNext { logger.debug { "Bound ${it.size} SpreadSheets" } }
                            .subscribe { items = it }

                    cellFormat { spreadSheet: SpreadSheet ->
                        this@cellFormat.text = spreadSheet.name
                    }
                }
            }
        }

        separator()

        fieldset {
            enableWhen(selectedSpreadSheet.isNotNull)
            fieldset("Choose Columns to assign to axises") {
                fieldset {
                    field("X Axis") {
                        combobox<DataColumn>(property = model.xAxisColumn) {
                            selectedSpreadSheet
                                    .toObservable()
                                    .map { it.dataTable.columns }
                                    .doOnNext { logger.debug { "Bound ${it.size} SpreadSheet columns to X Axis" } }
                                    .subscribe { items = it }

                            cellFormat { dataColumn: DataColumn ->
                                this@cellFormat.text = dataColumn.name
                            }

                            required()
                        }
                    }
                }

                fieldset {
                    field("Y Axis") {
                        combobox<DataColumn>(property = model.yAxisColumn) {
                            selectedSpreadSheet
                                    .toObservable()
                                    .map { it.dataTable.columns }
                                    .doOnNext { logger.debug { "Bound ${it.size} SpreadSheet columns to Y Axis" } }
                                    .subscribe { items = it }

                            cellFormat { dataColumn: DataColumn ->
                                this@cellFormat.text = dataColumn.name
                            }

                            required()
                        }
                    }
                }
            }


//            fieldset("Classes") {
//
//            }
        }

        buttonbar {
            button("Cancel") {

            }
            button("Create") {
                shortcut(KeyCodeCombination(KeyCode.ENTER))
                enableWhen(model.valid)
                action { find(Chart2DModal::class).openWindow() }
            }
        }

    }

    init {
        /* Whenever a selectedSpreadSheet changes we want to clear the rest of the form, because x and y axis tables might not match */
        selectedSpreadSheet
                .toObservable()
                .doOnNext { logger.debug { "Clearing ViewModel, because a new SpreadSheed is selected: '${it.name}'" } }
                .subscribe { model.rollback() }


        /* if selelectedSpreadSheet was passed as a parameter, then I set it as a selected spreadsheet */
        params["selectedSpreadSheet"].apply {
            if (this === null) {
                return@apply
            }
            selectedSpreadSheet.set(this as SpreadSheet)
        }
    }

    class Chart2DConfigViewModel : ViewModel() {
        val xAxisColumn = bind { SimpleObjectProperty<DataColumn>() }
        val yAxisColumn = bind { SimpleObjectProperty<DataColumn>() }
    }
}

