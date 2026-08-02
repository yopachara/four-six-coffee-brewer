package com.yopachara.core.util

import com.yopachara.fourtosixmethod.util.roundTo
import java.util.Locale
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NumberTest {

    private val original = Locale.getDefault()

    @Before
    fun setCommaDecimalLocale() {
        // de-DE formats 18.7 as "18,7" — the locale that used to crash roundTo
        Locale.setDefault(Locale.GERMANY)
    }

    @After
    fun restoreLocale() {
        Locale.setDefault(original)
    }

    @Test
    fun floatRoundTo_worksInCommaDecimalLocale() {
        // 18.8f - 0.1f == 18.700001f, the value the weight stepper feeds in
        assertEquals(18.7f, (18.8f - 0.1f).roundTo(1), 0.0001f)
        assertEquals(18.7f, 18.66f.roundTo(1), 0.0001f)
        assertEquals(18.6f, 18.64f.roundTo(1), 0.0001f)
        assertEquals(19f, 18.7f.roundTo(0), 0.0001f)
    }

    @Test
    fun doubleRoundTo_worksInCommaDecimalLocale() {
        assertEquals(18.7, 18.66.roundTo(1), 0.0001)
        assertEquals(1.23, 1.2345.roundTo(2), 0.0001)
    }
}
