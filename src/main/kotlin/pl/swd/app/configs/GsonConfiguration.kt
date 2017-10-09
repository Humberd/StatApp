package pl.swd.app.configs

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javafx.collections.ObservableList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.swd.app.serializers.ObservableListJsonDeserializer

@Configuration
open class GsonConfiguration {
    @Bean
    open fun createGson(): Gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ObservableList::class.java, ObservableListJsonDeserializer())
            .create()
}