package pl.swd.app.serializers

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.*
import javafx.collections.ObservableList
import pl.swd.app.models.Config
import pl.swd.app.models.Project
import java.lang.reflect.Type

object ConfigJsonSerializer : JsonSerializer<Config>, JsonDeserializer<Config> {
    override fun serialize(config: Config, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return jsonObject(
                "lastOpenedProjectFileName" to config.lastOpenedProjectFileName
        )
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Config {
        return Config(
                lastOpenedProjectFileName = context.deserialize(json.get("lastOpenedProjectFileName"), String::class.java)
        )
    }

}