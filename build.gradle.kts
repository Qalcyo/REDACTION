@file:Suppress("UnstableApiUsage", "PropertyName")

import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.minecraft.loom")
}

toolkitLoomHelper {
    // Adds OneConfig to our project
    useOneConfig(mcData, "commands", "config", "config-impl", "events", "internal", "ui")

    // Removes the server configs from IntelliJ IDEA, leaving only client runs.
    // If you're developing a server-side mod, you can remove this line.
    disableRunConfigs(GameSide.SERVER)

    // Sets up our Mixin refmap naming
    if (!mcData.isNeoForge) {
        useMixinRefMap(modData.id)
    }

    // Adds the tweak class if we are building legacy version of forge as per the documentation (https://docs.polyfrost.org)
    if (mcData.isLegacyForge) {
        useTweaker("org.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker", GameSide.CLIENT)
        useForgeMixin(modData.id) // Configures the mixins if we are building for forge, useful for when we are dealing with cross-platform projects.

        useProperty("mixin.debug.export", "true", GameSide.CLIENT)
    }
}

// Configures the Polyfrost Loom, our plugin fork to easily set up the programming environment.
if (mcData.isForge) {
    loom {
        forge {
            accessTransformer(rootProject.file("src/main/resources/redaction_at.cfg"))
        }
    }
}

// Configures the output directory for when building from the `src/resources` directory.
sourceSets {
    val dummy by creating
    main {
        compileClasspath += dummy.output
        output.setResourcesDir(java.classesDirectory)
    }
}

// Adds the Polyfrost maven repository so that we can get the libraries necessary to develop the mod.
repositories {
    maven("https://repo.polyfrost.org/releases")
}

// Configures the libraries/dependencies for your mod.
dependencies {
    // If we are building for legacy forge, includes the launch wrapper with `shade` as we configured earlier, as well as mixin 0.7.11
    if (mcData.isLegacyForge) {
        compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    }
}

tasks {
    // Processes the `src/resources/mcmod.info`, `fabric.mod.json`, or `mixins.${mod_id}.json` and replaces
    // the mod id, name and version with the ones in `gradle.properties`
    processResources {
        rename("(.+_at.cfg)", "META-INF/$1")
    }

    jar {
        // Sets the jar manifest attributes.
        if (mcData.isLegacyForge) {
            manifest.attributes += mapOf(
                "FMLAT" to "redaction_at.cfg"
            )
        }
    }
}