package pl.swd.app.configs

import com.google.gson.Gson
import mu.KLogging
import org.hildan.fxgson.FxGson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.swd.app.models.Project
import pl.swd.app.models.SpreadSheet
import pl.swd.app.serializers.ProjectJsonSerializer
import pl.swd.app.serializers.SpreadSheetJsonSerializer

@Configuration
open class GsonConfiguration {
    companion object : KLogging()

    @Bean
    open fun gson(): Gson = FxGson.coreBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Project::class.java, ProjectJsonSerializer)
            .registerTypeAdapter(SpreadSheet::class.java, SpreadSheetJsonSerializer)
            .create()
}

