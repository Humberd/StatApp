package pl.swd.app.models

import javafx.scene.chart.Axis

data class Chart2dData<X, Y>(
        val chartType: String,
        val xAxis: Axis<X>,
        val yAxis: Axis<Y>,
        val xAxisData: List<X>,
        val yAxisData: List<Y>
)