package pl.swd.app.serializers

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = arrayOf("/test-beans.xml"))
class SimplePropertySerializerTest {

    @Autowired
    lateinit var gson: Gson

    @Test
    fun foo () {
        println(gson)
    }
}