package pl.swd.app.views

import tornadofx.*

class AppMainView : View("My View") {
    override val root = borderpane {
        top(AppMenuBar::class)
    }
}
