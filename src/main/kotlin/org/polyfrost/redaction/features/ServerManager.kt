package org.polyfrost.redaction.features

//#if FORGE
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
//#else
//$$ import net.legacyfabric.fabric.api.client.networking.v1.ClientPlayConnectionEvents
//#endif

import org.polyfrost.oneconfig.api.ui.v1.Notifications
import org.polyfrost.oneconfig.utils.v1.JsonUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.redaction.Redaction
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.universal.UMinecraft

object ServerManager {

    private var lastCacheAttempt = 0L
    private val serverList = hashMapOf<String, String>()

    fun initialize() {
        //#if FORGE
        MinecraftForge.EVENT_BUS.register(this)
        //#else
        //$$ ClientPlayConnectionEvents.JOIN.register(ClientPlayConnectionEvents.Join { _, _, _ ->
        //$$     saveLastServerIp()
        //$$ })
        //#endif

        if (RedactionConfig.serverPreview) {
            Multithreading.submit {
                cacheServerNames()
            }
        }
    }

    fun getServerName(ip: String): String {
        if (serverList.isEmpty() && System.currentTimeMillis() - lastCacheAttempt > 10_000) {
            lastCacheAttempt = System.currentTimeMillis()

            // Block until the cache is populated so that we can return an actual value
            // This check is here in case the initial request either fails or if the user disabled the feature before startup but enabled it later
            cacheServerNames()
        }

        return serverList.entries.find { entry -> ip.endsWith(entry.key, ignoreCase = true) }?.value ?: ip
    }

    //#if FORGE
    @SubscribeEvent
    fun onServerJoined(event: ClientConnectedToServerEvent) {
        saveLastServerIp()
    }
    //#endif

    private fun saveLastServerIp() {
        if (!UMinecraft.getMinecraft().isIntegratedServerRunning) { // Don't save the IP if we're connected/connecting to a singleplayer world
            RedactionConfig.lastServerIP = UMinecraft.getMinecraft().currentServerData?.serverIP ?: ""
            RedactionConfig.save()
        }
    }

    private fun cacheServerNames() {
        val json = JsonUtils.parseFromUrl("https://servermappings.lunarclientcdn.com/servers.json")
        if (json == null) {
            Notifications.enqueue(Notifications.Type.Warning, Redaction.NAME, "Failed to cache server names for direct connect preview: Could not fetch/parse")
            return
        }

        if (!json.isJsonArray) {
            Notifications.enqueue(Notifications.Type.Warning, Redaction.NAME, "Failed to cache server names for direct connect preview: Unexpected JSON format")
            return
        }

        for (element in json.asJsonArray) {
            if (!element.isJsonObject) {
                continue
            }

            val serverJson = element.asJsonObject
            val addressesElement = serverJson["addresses"] ?: continue
            if (!addressesElement.isJsonArray) {
                continue
            }

            for (address in addressesElement.asJsonArray) {
                if (!address.isJsonPrimitive) {
                    continue
                }

                val name = serverJson["name"] ?: continue
                if (!name.isJsonPrimitive) {
                    continue
                }

                serverList[address.asString] = name.asString
            }
        }
    }

}