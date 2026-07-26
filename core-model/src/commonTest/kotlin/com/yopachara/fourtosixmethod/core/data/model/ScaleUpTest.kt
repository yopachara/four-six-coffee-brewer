package com.yopachara.fourtosixmethod.core.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * `scaleUp`/`formatScaleUp` replaced `BigDecimal.setScale(scale, RoundingMode.UP)` when the model
 * moved to commonMain, so these pin down the rounding-away-from-zero behaviour they have to keep.
 */
class ScaleUpTest {

    @Test
    fun formatScaleUp_rounds_away_from_zero() {
        assertEquals("36.1", 36.04f.formatScaleUp(1))
        assertEquals("36.1", 36.05f.formatScaleUp(1))
        assertEquals("36.0", 36.0f.formatScaleUp(1))
    }

    @Test
    fun formatScaleUp_always_renders_the_requested_fraction_digits() {
        assertEquals("36.00", 36f.formatScaleUp(2))
        assertEquals("36.10", 36.1f.formatScaleUp(2))
        // A carried fraction must not lose its leading zero.
        assertEquals("36.01", 36.001f.formatScaleUp(2))
        assertEquals("37", 36.2f.formatScaleUp(0))
    }

    @Test
    fun formatScaleUp_keeps_the_sign_but_not_for_negative_zero() {
        assertEquals("-36.1", (-36.04f).formatScaleUp(1))
        assertEquals("0.0", (-0.0f).formatScaleUp(1))
    }

    @Test
    fun scaleUp_matches_formatScaleUp() {
        assertEquals(36.1f, 36.04f.scaleUp(1))
        assertEquals(36.0f, 36.0f.scaleUp(1))
        assertEquals(-36.1f, (-36.04f).scaleUp(1))
    }
}
