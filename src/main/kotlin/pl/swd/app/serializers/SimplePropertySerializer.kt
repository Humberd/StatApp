package pl.swd.app.serializers

import com.google.gson.*
import javafx.beans.property.*
import pl.swd.app.exceptions.UnsupportedGsonPropertyType
import pl.swd.app.utils.isInstanceOf
import java.lang.reflect.Type

object SimplePropertySerializer : JsonSerializer<Property<*>>, JsonDeserializer<Property<*>> {
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