package pl.swd.app

import javafx.application.Application
import javafx.scene.chart.NumberAxis
import javafx.stage.Stage
import mu.KLogging
import org.springframework.context.support.ClassPathXmlApplicationContext
import pl.swd.app.configs.InitConfiguration
import pl.swd.app.models.Chart2dData
import pl.swd.app.views.MainView
import pl.swd.app.views.modals.Chart2DModal
import tornadofx.*
import kotlin.reflect.KClass

class StatApp : App(MainView::class) {
    companion object : KLogging()

    /**
     * Here I'm initializing a Spring application context,
     * so that I can inject Spring beans using: `by di()`
     */
    override fun init() {
        super.init()
        val springApplicationContext = ClassPathXmlApplicationContext("beans.xml")
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T {
                return springApplicationContext.getBean(type.java)
            }

            override fun <T : Any> getInstance(type: KClass<T>, name: String): T {
                return springApplicationContext.getBean(type.java, name)
            }
        }
    }

    override fun start(stage: Stage) {
        super.start(stage)

        with(stage) {
            width = 850.0
            height = 700.0

            centerOnScreen()
        }

        find(Chart2DModal::class, params = mapOf(
                Chart2DModal::chart2dData to Chart2dData(
                        chartType = "Linear",
                        xAxis = NumberAxis(),
                        xAxisData = arrayListOf(1, 2, 3),
                        yAxis = NumberAxis(),
                        yAxisData = arrayListOf(4, 2, 3)

                )
        )).openWindow()
    }

    override fun stop() {
        val initConfiguration = FX.dicontainer?.getInstance(InitConfiguration::class)
        initConfiguration?.destroy()

        super.stop()
    }
}

fun main(args: Array<String>) {
    Application.launch(StatApp::class.java, *args)
}