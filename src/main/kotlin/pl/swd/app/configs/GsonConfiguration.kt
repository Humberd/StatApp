package pl.swd.app.configs

import com.google.gson.*
import javafx.beans.property.*
import javafx.collections.ObservableList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.swd.app.exceptions.UnsupportedGsonPropertyType
import pl.swd.app.serializers.ObservableListJsonSerializer
import pl.swd.app.utils.emptyOptional
import pl.swd.app.utils.isInstanceOf
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.*

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
//            .setFieldNamingStrategy { f: Field ->
//                if (f.name.endsWith("\$delegate")) {
//                    return@setFieldNamingStrategy f.name.dropLast(9)
//                }
//                return@setFieldNamingStrategy f.name
//            }
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
            else -> throw UnsupportedGsonPropertyType("Cannot seria")
        }
    }

    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Property<*> {
        type.isInstanceOf(SimpleStringProperty::class)

        if (type.isInstanceOf(SimpleBooleanProperty::class)) {
            return SimpleBooleanProperty(json.asBoolean)
        } else if (type.isInstanceOf(SimpleStringProperty::class)) {
            return SimpleStringProperty(json.asString)
        } else if (type.isInstanceOf(SimpleDoubleProperty::class)) {
            return SimpleDoubleProperty(json.asDouble)
        } else if (type.isInstanceOf(SimpleFloatProperty::class)) {
            return SimpleFloatProperty(json.asFloat)
        } else if (type.isInstanceOf(SimpleLongProperty::class)) {
            return SimpleLongProperty(json.asLong)
        } else if (type.isInstanceOf(SimpleIntegerProperty::class)) {
            return SimpleIntegerProperty(json.asInt)
        } else {
            throw UnsupportedGsonPropertyType("Cannot deserialize value: '$json' to type: (${type.typeName})")
        }

    }
}