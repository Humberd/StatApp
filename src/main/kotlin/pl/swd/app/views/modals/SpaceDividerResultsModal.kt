package pl.swd.app.views.modals

import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TableView
import mu.KLogging
import pl.swd.app.services.SpaceDivider.IterationsAlreadyCompletedException
import pl.swd.app.services.SpaceDivider.PointsToRemoveIn1CutResponse
import pl.swd.app.services.SpaceDivider.SpaceDividerPoint
import pl.swd.app.services.SpaceDivider.SpaceDividerService
import pl.swd.app.utils.emptyObservableList
import tornadofx.*
import java.io.File

class SpaceDividerResultsModal : Modal("Space Divider Result") {
    companion object : KLogging()

    val spaceDividerService: SpaceDividerService by di()
    val pointsList: List<SpaceDividerPoint> by param()
    val showChart: Boolean by param()
    var resultsTable: TableView<PointsToRemoveIn1CutResponse> by singleAssign()
    var worker: SpaceDividerService.SpaceDividerWorker? = null

    override val root = borderpane {
        center {
            if (showChart) {
vbox {
    add(ScatterChart(NumberAxis(), NumberAxis()).apply {
        val seriesMap: HashMap<String, XYChart.Series<Number, Number>> = HashMap()

        pointsList
                .map { it.decisionClass }
                .distinct()
                .forEach {
                    seriesMap.put(it, XYChart.Series())
                }

        for (point in pointsList) {
            seriesMap.get(point.decisionClass)?.data(point.axisesValues[0], point.axisesValues[1])
        }

        seriesMap
                .toSortedMap()
                .forEach { key, value ->
                    value.name = key
                    data.add(value)
                }
        (xAxis as NumberAxis).setForceZeroInRange(false)
        (yAxis as NumberAxis).setForceZeroInRange(false)
    })
}
            }
        }
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