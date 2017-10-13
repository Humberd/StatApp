package pl.swd.app.serializers;

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import javafx.collections.ObservableList
import pl.swd.app.exceptions.GenericTypeNotSpecifiedException
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import tornadofx.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Gson doesn't know how to deserialise json -> ObservableList<*>
 * This is why we need to teach him how to do it.
 */
object ObservableListJsonSerializer : JsonDeserializer<ObservableList<*>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ObservableList<*> {
        /**
         *  When the typeofT is 'type of: ObservableList<Project>'
         *  Then this value is 'type of: Project'
         */
        val genericParamType = (typeOfT as ParameterizedType).actualTypeArguments.run {
            if (size == 0) {
                throw GenericTypeNotSpecifiedException("Type (${typeOfT.typeName}) should have 1 generic type, but it now has none")
            }
            get(0)
        }

        /**
         * If genericParamType is 'type of: Project'
         * Then this value is 'type of: ArrayList<Project>'
         */
        val arrayListGenericTyped = ParameterizedTypeImpl.make(ArrayList::class.java, arrayOf(genericParamType), null)

        /**
         * Converting json -> ArrayList<Project>
         */
        val list = context?.deserialize<List<*>>(json, arrayListGenericTyped)!!

        return list.observable()
    }

}