package pl.swd.app.configs

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javafx.beans.property.Property
import javafx.collections.ObservableList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.swd.app.serializers.ObservableListJsonSerializer
import pl.swd.app.serializers.SimplePropertySerializer

@Configuration
open class GsonConfiguration {
    @Bean
    open fun gson(): Gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ObservableList::class.java, ObservableListJsonSerializer)
            .registerTypeHierarchyAdapter(Property::class.java, SimplePropertySerializer)
//            .setFieldNamingStrategy { f: Field ->
//                if (f.name.endsWith("\$delegate")) {
//                    return@setFieldNamingStrategy f.name.dropLast(9)
//                }
//                return@setFieldNamingStrategy f.name
//            }
//            .registerTypeAdapter(Project::class.java, ProjectJsonSerializer)
            .create()
}

