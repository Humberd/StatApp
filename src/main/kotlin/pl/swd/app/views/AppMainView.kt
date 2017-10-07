package pl.swd.app.views

import tornadofx.*

class AppMainView : View("Stat App") {
    override val root = borderpane {
        top(AppMenuBar::class)
        center(AppTabs::class)
    }
}
