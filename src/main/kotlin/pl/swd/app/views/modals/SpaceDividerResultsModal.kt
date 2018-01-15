package pl.swd.app.views.modals

import javafx.scene.control.TableView
import mu.KLogging
import pl.swd.app.services.SpaceDivider.IterationsAlreadyCompletedException
import pl.swd.app.services.SpaceDivider.PointsToRemoveIn1CutResponse
import pl.swd.app.services.SpaceDivider.SpaceDividerPoint
import pl.swd.app.services.SpaceDivider.SpaceDividerService
import pl.swd.app.utils.emptyObservableList
import tornadofx.*

class SpaceDividerResultsModal : Modal("Space Divider Result") {
    companion object : KLogging()

    val spaceDividerService: SpaceDividerService by di()
    val pointsList: List<SpaceDividerPoint> by param()
    var resultsTable: TableView<PointsToRemoveIn1CutResponse> by singleAssign()
    var worker: SpaceDividerService.SpaceDividerWorker? = null

    override val root = borderpane {
        right {
            vbox {
                button("Next Iteration") {
                    action {
                        try {
                            resultsTable.items.add(worker?.nextIteration())
                        } catch (e: IterationsAlreadyCompletedException) {
                            showAllIterationsCompletedDialog()
                        }
                    }
                }
                button("Complete All Iterations") {
                    action {
                        val results = worker?.completeAllIterations()
                        if (results?.isEmpty()!!) {
                            showAllIterationsCompletedDialog()
                            return@action
                        }

                        resultsTable.items.addAll(results)
                    }
                }
                button("Reset") {
                    action {
                        initializeAlgorithm()
                    }
                }

                separator()

                resultsTable = tableview {
                    column("Cut Line", PointsToRemoveIn1CutResponse::class) {
                        value { cellDataFeatures ->
                            "${(cellDataFeatures.value.axisIndex + 97).toChar()} = ${cellDataFeatures.value.cutLineValue}"
                        }
                    }
                }
            }
        }
    }

    init {
        initializeAlgorithm()
    }

    fun initializeAlgorithm() {
        worker = spaceDividerService.initializeAlgorithm(pointsList)
        resultsTable.items = emptyObservableList()
    }

    fun showAllIterationsCompletedDialog() {
        information("No more iterations",
                "The are not points left to cut. Algorithm completed")
    }
}