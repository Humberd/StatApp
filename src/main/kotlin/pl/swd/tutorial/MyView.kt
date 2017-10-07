package pl.swd.tutorial

import tornadofx.*

class MyView1: View() {
    val api: Rest by inject()

    init {
        println(api.get("https://google.com"))
    }

    override val root = vbox {
        button("Open MyView2") {
            action {
                find<MyView2>(mapOf(MyView2::customer to "dupa")).openWindow();
            }
        }
    }
}

class MyView2: Fragment() {
    val customer: String by param()

    override val root = vbox {
        button("Print") {
            action {
                println(customer)
            }
        }
    }
}