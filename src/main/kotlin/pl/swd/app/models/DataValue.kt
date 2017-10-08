package pl.swd.app.models;

import javafx.beans.property.*
import tornadofx.*
import java.security.InvalidParameterException

class DataValue(val value: String) {
//    val valueProperty = when (value) {
//        is String -> SimpleStringProperty(this, "value", value)
//        is Int -> SimpleIntegerProperty(this, "value", value)
//        is Long -> SimpleLongProperty(this, "value", value)
//        is Double -> SimpleDoubleProperty(this, "value", value)
//        is Float -> SimpleFloatProperty(this, "value", value)
//        is Boolean -> SimpleBooleanProperty(this, "value", value)
//        else -> throw InvalidParameterException("Data value can only be a String, Integer, Long, Double, Float or Boolean")
//    }
//
//
//    val value by valueProperty
}