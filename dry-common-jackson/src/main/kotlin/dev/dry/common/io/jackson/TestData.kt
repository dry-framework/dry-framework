package dev.dry.common.io.jackson

import dev.dry.common.io.jackson.TestData.TestGender.MALE
import java.io.InputStream
import java.nio.ByteBuffer
import kotlin.reflect.jvm.javaType


data class TestData(val name: String, val mobileNumber: String, val gender: TestGender) {
    enum class TestGender { MALE }

    companion object {
        val TEST_DATA_OBJECT = TestData("Fred","123456789", MALE)
        const val TEST_DATA_STRING = """{"name":"Fred","mobileNumber":"123456789","gender":"MALE"}"""

        fun testDataBuffer(): ByteBuffer = ByteBuffer.wrap(TEST_DATA_STRING.toByteArray())
        fun testDataStream(): InputStream = TEST_DATA_STRING.byteInputStream()

        private fun testConsumer(data: TestData) {}
        fun getReflectedClass(): Class<*> {
            val parameter = Companion::testConsumer.parameters.find { it.name == "data" } //{ it.kind == KParameter.Kind.VALUE }
                ?: throw RuntimeException("reflected parameter not found")
            return parameter.type.javaType as Class<*>
        }
    }
}
