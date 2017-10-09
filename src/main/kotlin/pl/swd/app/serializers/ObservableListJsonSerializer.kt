package pl.swd.app.serializers;

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import javafx.collections.ObservableList
import tornadofx.*
import java.lang.reflect.Type
import java.util.*

class ObservableListJsonSerializer : JsonDeserializer<ObservableList<Any>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ObservableList<Any> {
        val list = context?.deserialize<List<Any>>(json, ArrayList::class.java)!!

        return list.observable()
    }

}