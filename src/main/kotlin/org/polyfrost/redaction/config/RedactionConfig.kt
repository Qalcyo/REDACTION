package org.polyfrost.redaction.config

import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Checkbox
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.polyui.color.PolyColor
import org.polyfrost.polyui.color.argb
import org.polyfrost.redaction.Redaction
import org.polyfrost.redaction.features.particles.ParticleManager

object RedactionConfig : Config(
    "${Redaction.ID}.json",
    Redaction.NAME,
        Category.QOL,

) {

    @Switch(
        title = "Disable Hand Item Lighting",
        category = "General"
    )
    var disableHandLighting = false

    @Switch(
        title = "Customize Hand Item FOV",
        category = "General"
    )
    var customHandFOV = false

    @Slider(
        title = "Hand Item FOV",
        category = "General",
        min = 0F,
        max = 180F
    )
    var handFOV = 125

    @Switch(
        title = "Server Preview in Direct Connect",
        category = "General"
    )
    var serverPreview = false

    @Checkbox(
        title = "Last Server Joined Button",
        category = "General"
    )
    var lastServerJoined = false


    var lastServerIP = ""

    @Switch(
        title = "Replace Hotbar with Blackbar",
        category = "Blackbar"
    )
    var blackbar = false

    @Switch(
        title = "Add Snow in Inventory",
        category = "Inventory",
        subcategory = "Snow"
    )
    var addSnow = false

//    @Info(
//        text = "Higher snow amounts may result in reduced performance!",
//        size = 2,
//        category = "Inventory",
//        subcategory = "Snow",
//        type = InfoType.WARNING
//    )
//    var ignored = false

    @Slider(
        title = "Snow Amount",
        category = "Inventory",
        subcategory = "Snow",
        min = 50F,
        max = 1000F
    )
    var particles = 100

    @org.polyfrost.oneconfig.api.config.v1.annotations.Color(
        title = "Snow Color",
        category = "Inventory",
        subcategory = "Snow"
    )
    var snowColor = argb(-1)

    @Switch(
        title = "Draw Lines between Snowflakes",
        category = "Inventory",
        subcategory = "Lines"
    )
    var connectSnow = false

    @Slider(
        title = "Line Width",
        category = "Inventory",
        subcategory = "Lines",
        min = 1f,
        max = 5f,
        step = 1f
    )
    var lineWidth = 1f

    @org.polyfrost.oneconfig.api.config.v1.annotations.Color(
        title = "Line Color",
        category = "Inventory",
        subcategory = "Lines"
    )
    var lineColor = PolyColor.WHITE

    init {
        addCallback("particles") {
            ParticleManager.hasChanged = true
        }

        addDependency("handFOV", "customHandFOV")

        addDependency("particles", "addSnow")
        addDependency("snowColor", "addSnow")

        addDependency("connectSnow", "addSnow")
        addDependency("lineWidth", "addSnow")
        addDependency("lineWidth", "connectSnow")
        addDependency("lineColor", "addSnow")
        addDependency("lineColor", "connectSnow")

    }

}