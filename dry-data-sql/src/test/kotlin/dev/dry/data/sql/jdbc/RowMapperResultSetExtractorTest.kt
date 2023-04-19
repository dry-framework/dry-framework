package dev.dry.data.sql.jdbc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import java.sql.ResultSet


class RowMapperResultSetExtractorTest {
    class TestBean(val label: String, val value: Int)

    private val resultSet: ResultSet = mock()

    private val rowMapper = RowMapper { rs: ResultSet, _ ->
            TestBean(rs.getString(1), rs.getInt(2))
        }

    private val unit = RowMapperResultSetExtractor(rowMapper)

    @BeforeEach
    fun setUp() {
        given(resultSet.next()).willReturn(true, true, true, false)
        given(resultSet.getString(1)).willReturn("Zero", "One", "Two")
        given(resultSet.getInt(2)).willReturn(0, 1, 2)
    }

    @Test
    fun extract() {
        val result = unit.extract(resultSet)
        assertThat(result).hasSize(3)
        val testBean0 = result[0]
        assertThat(testBean0.label).isEqualTo("Zero")
        assertThat(testBean0.value).isEqualTo(0)
        val testBean1 = result[1]
        assertThat(testBean1.label).isEqualTo("One")
        assertThat(testBean1.value).isEqualTo(1)
        val testBean2 = result[2]
        assertThat(testBean2.label).isEqualTo("Two")
        assertThat(testBean2.value).isEqualTo(2)
    }
}
