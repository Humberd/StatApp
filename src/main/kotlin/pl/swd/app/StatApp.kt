package pl.swd.app

import javafx.application.Application
import javafx.stage.Stage
import pl.swd.app.views.MainView
import tornadofx.*

class StatApp : App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)

        with(stage) {
            width = 700.0
            height = 600.0

            centerOnScreen()
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(StatApp::class.java, *args)
}