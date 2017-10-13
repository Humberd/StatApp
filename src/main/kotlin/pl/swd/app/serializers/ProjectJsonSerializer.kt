package pl.swd.app.serializers

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.*
import javafx.collections.ObservableList
import pl.swd.app.models.Project
import java.lang.reflect.Type

object ProjectJsonSerializer : JsonSerializer<Project>, JsonDeserializer<Project> {
    override fun serialize(project: Project, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return jsonObject(
                "name" to project.name,
                "spreadSheetList" to context?.serialize(project.spreadSheetList)
        )
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Project {
        return Project(
                name = context.deserialize(json.get("name"), String::class.java),
                spreadSheetList = context.deserialize(json.get("spreadSheetList"), ObservableList::class.java)
        )
    }

}
