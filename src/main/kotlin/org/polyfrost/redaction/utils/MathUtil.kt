package org.polyfrost.redaction.utils

object MathUtil {

    private fun clamp01(value: Float): Float {
        if (value.toDouble() < 0.0) return 0.0f
        return if (value.toDouble() > 1.0) 1f else value
    }

}