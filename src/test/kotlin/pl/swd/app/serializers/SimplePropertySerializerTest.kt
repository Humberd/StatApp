package pl.swd.app.serializers

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = arrayOf("/test-beans.xml"))
class SimplePropertySerializerTest {

    @Autowired
    lateinit var gson: Gson

    @Test
    fun `should serialize and deserialize mapOf`() {
        val mapMock = MapGsonMock()
        val jsonString = gson.toJson(mapMock)
        val parsedMapMock = gson.fromJson<MapGsonMock>(jsonString)
        assertEquals(mapMock.foo.toString(), parsedMapMock.foo.toString())
    }
}

class MapGsonMock {
    val foo = mapOf(
            "one" to "1",
            "two" to "2"
    )
}