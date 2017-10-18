package pl.swd.app

import javafx.application.Application
import javafx.stage.Stage
import mu.KLogging
import org.springframework.context.support.ClassPathXmlApplicationContext
import pl.swd.app.configs.InitConfiguration
import pl.swd.app.views.MainView
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