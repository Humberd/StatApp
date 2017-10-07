package pl.swd.obs

import com.github.thomasnield.rxkotlinfx.events
import javafx.application.Application
import javafx.scene.input.KeyEvent
import tornadofx.*

class MyApp: App(MyView::class) {
}

class MyView: View() {
    override val root = vbox {
        listview<String> {
            (0..9).asSequence().map { it.toString() }.forEach { items.add(it) }


            events(KeyEvent.KEY_TYPED)
                    .map { it.character }
                    .filter { it.matches(Regex("[0-9]")) }
                    .subscribe { selectionModel.select(it)}
        }
    }

}

fun main(args: Array<String>) {
    Application.launch(MyApp::class.java, *args)
}