package pl.swd.app.utils;

import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.input.MouseEvent
import tornadofx.*
import java.util.*

/**
 * Returns empty ObservableList
 */
inline fun <reified T> emptyObservableList(): ObservableList<T> {
    return arrayListOf<T>().observable()
}


/**
 * Any object can be transformed to an optional
 */
fun <T> T?.asOptional() = Optional.ofNullable(this)
fun <T> emptyOptional(): Optional<T> = Optional.empty()


/**
 * ListView extension functions
 *
 * todo: add more extension functions to listview?
 */
fun <T> ListView<T>.onItemClick(action: (T) -> Unit) {
    addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
        if (selectedItem != null && event.target.isInsideRow()) {
            action(selectedItem!!)
        }
    }
}