package pl.swd.app.serializers

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import pl.swd.app.models.Project
import java.lang.reflect.Type

object ProjectJsonSerializer : JsonSerializer<Project> {
    override fun serialize(project: Project, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return jsonObject(
                "name" to project.name,
                "spreadSheetList" to context?.serialize(project.spreadSheetList)
        )
    }

}
