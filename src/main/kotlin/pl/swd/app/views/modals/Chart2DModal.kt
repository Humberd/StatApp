package pl.swd.app.views.modals

import io.reactivex.Observable
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import pl.swd.app.models.Chart2dData
import tornadofx.*

class Chart2DModal : Modal() {

    val chart2dData: Chart2dData<Number, Number> by param()
    var chart: LineChart<Number, Number> by singleAssign()

    override val root = vbox {
        chart = linechart("Foo", NumberAxis(), NumberAxis()) {
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
//            }
        }
    }

    init {
        val arr = arrayListOf<XYChart.Data<Number, Number>>()
        for (i in 0..chart2dData.xAxisData.size - 1) {
            arr.add(XYChart.Data(chart2dData.xAxisData[i], chart2dData.yAxisData[i]))
        }

        chart.series("MySeries", elements = arr.observable())
    }
}