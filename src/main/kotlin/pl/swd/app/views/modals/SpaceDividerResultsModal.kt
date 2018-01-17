package pl.swd.app.views.modals

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TableView
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
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
    var chart: BetterScatterChart<Number, Number> by singleAssign()

    override val root = borderpane {
        center {
            if (showChart) {
                vbox {
                    chart = object : BetterScatterChart<Number, Number>(NumberAxis(), NumberAxis()) {
                        val lines = arrayListOf<Shape>()

                        override fun layoutPlotChildren() {
                            super.layoutPlotChildren()
                            plotChildren.removeAll(lines)
                            lines.clear()
                            for (result in (worker?.iterationsResults ?: emptyList<PointsToRemoveIn1CutResponse>())) {
                                if (result.axisIndex == 0) {
                                    lines.add(
                                            Line(
                                                    xAxis.getDisplayPosition(result.cutLineValue),
                                                    height,
                                                    xAxis.getDisplayPosition(result.cutLineValue),
                                                    0.0)
                                    )
                                } else {
                                    lines.add(
                                            Line(
                                                    0.0,
                                                    yAxis.getDisplayPosition(result.cutLineValue),
                                                    width,
                                                    yAxis.getDisplayPosition(result.cutLineValue))
                                    )
                                }
                            }
                            plotChildren.addAll(lines)
                            logger.trace { "Plot refresh" }
                        }
                    }.apply {
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
                    }
                    add(chart)
                }
            }
        }
        right {
            vbox {
                button("Next Iteration") {
                    actionEvents()
                            .observeOn(Schedulers.computation())
                            .flatMap {
                                try {
                                    Observable.just(listOf(worker?.nextIteration()))
                                } catch (e: IterationsAlreadyCompletedException) {
                                    Observable.just(emptyList<PointsToRemoveIn1CutResponse>())
                                }
                            }
                            .observeOnFx()
                            .doOnNext { results ->
                                if (results.isEmpty()) {
                                    showAllIterationsCompletedDialog()
                                    return@doOnNext
                                }

                                resultsTable.items.addAll(results)
                                refreshChart()
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
                                refreshChart()
                            }
                            .subscribe()
                }
                button("Reset") {
                    action {
                        initializeAlgorithm()
                        refreshChart()
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

    fun refreshChart() {
        if (showChart) {
            chart.refresh()
        }
    }

    fun initializeAlgorithm() {
        val newList = ArrayList(pointsList)
                .map { it.copy(vector = arrayListOf()) }
        worker = spaceDividerService.initializeAlgorithm(newList)
        resultsTable.items = emptyObservableList()
    }

    fun showAllIterationsCompletedDialog() {
        information("No more iterations",
                "The are not points left to cut. Algorithm completed")
    }
}

open class BetterScatterChart<T, P>(xAxis: Axis<T>?, yAxis: Axis<P>?) : ScatterChart<T, P>(xAxis, yAxis) {
    fun refresh() {
        requestChartLayout()
    }
}