package org.polyfrost.redaction.features.particles

import net.minecraft.client.Minecraft
import org.polyfrost.redaction.utils.RenderUtils
import org.polyfrost.universal.UResolution
import kotlin.random.Random

/**
 * Particle API
 * This Api is free2use
 * But u have to mention me.
 *
 * @author Vitox
 * @version 3.0
 */
class Particle internal constructor(var x: Float, var y: Float) {

    val size: Float = 0.3f + RANDOM.nextFloat() * 1.3f
    private val ySpeed = RANDOM.nextFloat() * 0.5f
    private val xSpeed = RANDOM.nextFloat() * 0.5f

    fun connect(x: Float, y: Float, color: Int, width: Float) {
        RenderUtils.connectPoints(this.x, this.y, x, y, color, width)
    }

    fun update() {
        y += ySpeed
        x += xSpeed

        val mc = Minecraft.getMinecraft()
        if (y > mc.displayHeight) y = 1f
        if (x > mc.displayWidth) x = 1f
        if (x < 1) x = UResolution.scaledWidth.toFloat()
        if (y < 1) y = UResolution.scaledHeight.toFloat()
    }

    companion object {
        private val RANDOM = Random.Default
    }

}