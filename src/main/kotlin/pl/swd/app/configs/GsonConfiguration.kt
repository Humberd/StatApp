package pl.swd.app.configs

import com.google.gson.*
import javafx.beans.property.*
import javafx.collections.ObservableList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.swd.app.exceptions.UnsupportedGsonPropertyType
import pl.swd.app.models.Config
import pl.swd.app.models.Project
import pl.swd.app.serializers.ConfigJsonSerializer
import pl.swd.app.serializers.ObservableListJsonSerializer
import pl.swd.app.serializers.ProjectJsonSerializer
import pl.swd.app.serializers.SimplePropertySerializer
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
//            .registerTypeAdapter(Project::class.java, ProjectJsonSerializer)
//            .registerTypeAdapter(Config::class.java, ConfigJsonSerializer)
            .registerTypeHierarchyAdapter(Property::class.java, SimplePropertySerializer)
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

