package dev.dry.common.io.jackson

import dev.dry.common.io.jackson.TestData.Companion.TEST_DATA_OBJECT
import dev.dry.common.io.jackson.TestData.Companion.TEST_DATA_STRING
import dev.dry.common.io.jackson.TestData.Companion.getReflectedClass
import dev.dry.common.io.jackson.TestData.Companion.testDataStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class JacksonObjectMapperTest {
    companion object {
        val unit = JacksonObjectMapper.construct()
    }

    @Test
    fun readKClass() {
        val data = unit.read(testDataStream(), TestData::class)
        assertEquals(TEST_DATA_OBJECT, data)
    }

    @Test
    fun read() {
        val data = unit.read(testDataStream(), TestData::class.java)
        assertEquals(TEST_DATA_OBJECT, data)
    }

    @Test
    fun readWithReflectedClass() {
        val testDataClass = getReflectedClass()
        val data = unit.read(testDataStream(), testDataClass)
        assertEquals(TEST_DATA_OBJECT, data)
    }

    @Test
    fun write() {
        val output = ByteArrayOutputStream()
        unit.write(output, TEST_DATA_OBJECT)
        val data = String(output.toByteArray())
        assertEquals(TEST_DATA_STRING, data)
    }
}
