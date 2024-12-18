package org.polyfrost.redaction.features.blackbar

import net.minecraft.entity.player.EntityPlayer

interface HotbarRenderer {

    fun `redaction$renderHotbarItem`(index: Int, x: Int, y: Int, partialTicks: Float, player: EntityPlayer)

}
