package pl.swd.app.views;

import tornadofx.*

class AppMenuBar : View("My View") {

    override val root = menubar {
        menu("File") {
            item("Open", "Shortcut+O")
        }
    }
}
