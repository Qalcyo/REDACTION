package org.polyfrost.redaction.utils.particles

import net.minecraft.client.Minecraft
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.features.ParticleManager
import org.polyfrost.redaction.utils.RenderUtils
import kotlin.math.abs
import kotlin.random.Random

/**
 * Particle API This Api is free2use But u have to mention me.
 *
 * @author Vitox
 * @version 3.0
 */

class ParticleGenerator {

    private var particles: Array<Particle> = emptyArray()
    private var prevWidth = 0
    private var prevHeight = 0

    fun draw(mouseX: Int, mouseY: Int) {

        val mc = Minecraft.getMinecraft()
        val displayWidth = mc.displayWidth
        val displayHeight = mc.displayHeight

        if (particles.isEmpty() || prevWidth != displayWidth || prevHeight != displayHeight || ParticleManager.hasChanged) {
            createParticles(displayWidth, displayHeight)
        }

        prevWidth = displayWidth
        prevHeight = displayHeight

        val range = 50  // Akrz: 1/2 side length of square area around the cursor to connect particles in

        for (particle in particles) {

            particle.update()

            if (RedactionConfig.connectSnow){
                val mouseOver = mouseX in (particle.x - range).toInt()..(particle.x + range).toInt() &&
                        mouseY in (particle.y - range).toInt()..(particle.y + range).toInt()
                if (mouseOver) {
                    particles.filter { other ->
                        other !== particle && abs(other.x - particle.x) < range && abs(other.y - particle.y) < range
                    }.forEach { connectable ->
                        particle.connect(connectable.x, connectable.y, RedactionConfig.lineColor.rgba, RedactionConfig.lineWidth)
                    }
                }
            }

            RenderUtils.drawCircle(particle.x, particle.y, particle.size, RedactionConfig.snowColor.rgba)

        }

    }

    private fun createParticles(displayWidth: Int, displayHeight: Int) {

        val random = Random.Default
        particles = Array(RedactionConfig.particles * 4) {  // Akrz: Introduced a multiplier here to emulate the original functionality
            Particle(random.nextInt(displayWidth).toFloat(), random.nextInt(displayHeight).toFloat())
        }
        ParticleManager.hasChanged = false

    }

}