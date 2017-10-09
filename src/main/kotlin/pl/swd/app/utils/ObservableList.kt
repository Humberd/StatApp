package pl.swd.app.utils;

import javafx.collections.ObservableList
import tornadofx.*
import java.util.*

/**
 * Returns empty ObservableList
 */
inline fun <reified T> emptyObservableList(): ObservableList<T> {
    return arrayListOf<T>().observable()
}


fun <T> T?.asOptional() = Optional.ofNullable(this)
fun <T> emptyOptional(): Optional<T> = Optional.empty()