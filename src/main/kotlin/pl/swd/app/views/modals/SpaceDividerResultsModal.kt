package pl.swd.app.views.modals

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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
                    actionEvents()
                            .observeOn(Schedulers.computation())
                            .flatMap {
                                val value = try {
                                    listOf(worker?.nextIteration())
                                } catch (e: IterationsAlreadyCompletedException) {
                                    emptyList<PointsToRemoveIn1CutResponse>()
                                }

                                Observable.just(value)
                            }
                            .observeOnFx()
                            .doOnNext { results ->
                                if (results.isEmpty()) {
                                    showAllIterationsCompletedDialog()
                                    return@doOnNext
                                }

                                resultsTable.items.addAll(results)
                            }
                            .subscribe()
                }
                button("Complete All Iterations") {
                    actionEvents()
                            .observeOn(Schedulers.computation())
                            .map { worker?.completeAllIterations()!! }
                            .observeOnFx()
                            .doOnNext { results ->
                                if (results.isEmpty()) {
                                    showAllIterationsCompletedDialog()
                                    return@doOnNext
                                }

                                resultsTable.items.addAll(results)
                            }
                            .subscribe()
                }
                button("Reset") {
                    action {
                        initializeAlgorithm()
                    }
                }

                separator()

                resultsTable = tableview {
                    makeIndexColumn()
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

    override fun onDock() {
        super.onDock()
        currentWindow?.apply {
            width = if (showChart) 800.0 else 400.0
            centerOnScreen()
        }
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