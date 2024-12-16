package org.polyfrost.redaction.plugin

//#if FORGE && MC <= 1.12.2
import org.spongepowered.asm.lib.tree.ClassNode
//#else
//$$ import org.objectweb.asm.tree.ClassNode
//#endif

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class RedactionMixinPlugin : IMixinConfigPlugin {

    private var optifine = false

    override fun onLoad(mixinPackage: String?) {
        optifine = try {
            Class.forName("Config", false, javaClass.classLoader)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        return if (mixinClassName?.endsWith("_OptiFine") == true) optifine else true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {

    }

    override fun getMixins(): MutableList<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {

    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {

    }

}