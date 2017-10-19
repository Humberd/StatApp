package pl.swd.app.views.modals

import io.reactivex.Observable
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.layout.VBox
import mu.KLogging
import pl.swd.app.models.Chart2dData
import tornadofx.*
import java.util.*

class Chart2DModal : Modal() {
    companion object : KLogging()

    val chart2dData: Chart2dData by param()
    var box: VBox by singleAssign()

    override val root = borderpane {
        //        scatterchart("Foo", CategoryAxis(), NumberAxis()) {
//            series("Product X") {
//                data("MAR", 10245, 1000) {
//
//                }
//                data("APR", 23963, 5000)
//                data("MAY", 15038, 40000)
//            }
//            series("Product Y") {
//                data("MAR", 28443)
//                data("APR", 22845)
//                data("MAY", 19045)
//                data("MAY", 15045)
//            }
//        }
        center {
            box = vbox()
        }
    }

    init {
        val chart =
                /* X - Numbers, Y - Numbers */
                if (chart2dData.xAxis.numberValues !== null && chart2dData.yAxis.numberValues !== null) {
                    ScatterChart(
                            NumberAxis().apply { this.label = chart2dData.xAxis.title },
                            NumberAxis().apply { this.label = chart2dData.yAxis.title })
                            .apply {
                                val newSeries = series(chart2dData.title)

                                zipValues(chart2dData.xAxis.numberValues!!, chart2dData.yAxis.numberValues!!)
                                        .blockingSubscribe { pair -> newSeries.data(pair.first, pair.second) }
                            }
                }
                /* X - Numbers, Y - Strings */
                else if (chart2dData.xAxis.numberValues !== null && chart2dData.yAxis.stringValues !== null) {
                    ScatterChart(
                            NumberAxis().apply { this.label = chart2dData.xAxis.title },
                            CategoryAxis().apply { this.label = chart2dData.yAxis.title })
                            .apply {
                                val newSeries = series(chart2dData.title)

                                zipValues(chart2dData.xAxis.numberValues!!, chart2dData.yAxis.stringValues!!)
                                        .blockingSubscribe { pair -> newSeries.data(pair.first, pair.second) }
                            }
                }
                /* X - Strings, Y - Numbers */
                else if (chart2dData.xAxis.stringValues !== null && chart2dData.yAxis.numberValues !== null) {
                    ScatterChart(
                            CategoryAxis().apply { this.label = chart2dData.xAxis.title },
                            NumberAxis().apply { this.label = chart2dData.yAxis.title })
                            .apply {
                                val newSeries = series(chart2dData.title)

                                zipValues(chart2dData.xAxis.stringValues!!, chart2dData.yAxis.numberValues!!)
                                        .blockingSubscribe { pair -> newSeries.data(pair.first, pair.second) }
                            }
                }
                /* X- Strings, Y - String */
                else if (chart2dData.xAxis.stringValues !== null && chart2dData.yAxis.stringValues !== null) {
                    ScatterChart(
                            CategoryAxis().apply { this.label = chart2dData.xAxis.title },
                            CategoryAxis().apply { this.label = chart2dData.yAxis.title })
                            .apply {
                                val newSeries = series(chart2dData.title)

                                zipValues(chart2dData.xAxis.stringValues!!, chart2dData.yAxis.stringValues!!)
                                        .blockingSubscribe { pair -> newSeries.data(pair.first, pair.second) }
                            }
                } else {
                    throw Exception("Axises must have at leas one stringValues or numberValues, but now 1 or more has none")
                }

        chart.title = chart2dData.title
        box.add(chart)

    }

    private fun getLowerBound(list: List<Double>): Double {
        return Collections.min(list)
    }

    private fun getUpperBound(list: List<Double>): Double {
        return Collections.max(list)
    }

    private fun <A, B> zipValues(firstList: List<A>, secondList: List<B>): Observable<Pair<A, B>> {
        return Observable.fromIterable(firstList)
                .zipWith(secondList, { t1, t2 ->
                    Pair(t1, t2)
                })
    }
}