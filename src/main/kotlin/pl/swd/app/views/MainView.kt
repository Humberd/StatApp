package pl.swd.app.views

import tornadofx.*

class MainView : View("Stat App") {
    override val root = borderpane {
        top(MenuBarView::class)
        left(LeftDrawer::class)
        center(TabsView::class)
    }
}
