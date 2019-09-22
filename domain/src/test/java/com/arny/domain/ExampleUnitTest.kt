package com.arny.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(PowerMockRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ExampleUnitTest {
    @Test
    fun aa_abc() {
        val abc = columnName(29)
        val s = "AC"
        assertThat(abc).isEqualTo(s)
    }
}