package pl.swd.app.serializers

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(locations = arrayOf("/test-beans.xml"))
class SimplePropertySerializerTest {

    @Autowired
    lateinit var gson: Gson

    @Test
    fun foo () {
        println(gson)
    }
}