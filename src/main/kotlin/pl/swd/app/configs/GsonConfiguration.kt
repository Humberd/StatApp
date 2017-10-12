package pl.swd.app.configs

import com.google.gson.*
import javafx.beans.property.*
import javafx.collections.ObservableList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.swd.app.exceptions.UnsupportedGsonPropertyType
import pl.swd.app.serializers.ObservableListJsonSerializer
import pl.swd.app.utils.isInstanceOf
import java.lang.reflect.Field
import java.lang.reflect.Type

@Configuration
open class GsonConfiguration {
    @Bean
    open fun gson(): Gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ObservableList::class.java, ObservableListJsonSerializer)
            .registerTypeHierarchyAdapter(Property::class.java, foo)
//            .registerTypeAdapter(SimpleStringProperty::class.java, jsonSerializer { serializerArg: SerializerArg<SimpleStringProperty> ->
//                println(serializerArg.src)
//
//                jsonObject()
//            })
            .setFieldNamingStrategy { f: Field ->
                if (f.name.endsWith("\$delegate")) {
                    return@setFieldNamingStrategy f.name.dropLast(9)
                }
                return@setFieldNamingStrategy f.name
            }
//            .registerTypeAdapter(Project::class.java, ProjectJsonSerializer)
            .create()
}

object foo : JsonSerializer<Property<*>>, JsonDeserializer<Property<*>> {
    override fun serialize(src: Property<*>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return when (src) {
            is BooleanProperty -> JsonPrimitive(src.value)
            is StringProperty -> JsonPrimitive(src.value)
            is DoubleProperty -> JsonPrimitive(src.value)
            is FloatProperty -> JsonPrimitive(src.value)
            is LongProperty -> JsonPrimitive(src.value)
            is IntegerProperty -> JsonPrimitive(src.value)
            else -> throw UnsupportedGsonPropertyType()
        }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Property<*> {
        typeOfT?.isInstanceOf(SimpleStringProperty::class)

        when (typeOfT?.typeName)

        return context?.deserialize(json, typeOfT)!!
    }
}