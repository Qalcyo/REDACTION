package org.polyfrost.redaction.features

import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.oneconfig.utils.v1.NetworkUtils
import org.polyfrost.redaction.config.RedactionConfig

object ServerManager {

    private val serverList = hashMapOf<String, String>()

    fun initialize() {
        Multithreading.submit {
            val json =
                JsonParser().parse(NetworkUtils.getString("https://servermappings.lunarclientcdn.com/servers.json")).asJsonArray
            for (element in json) {
                val serverJson = element.asJsonObject
                val addresses = serverJson["addresses"].asJsonArray
                for (address in addresses) {
                    serverList[address.asString] = serverJson["name"].asString
                }
            }
        }
    }

    fun getNameOfServer(ip: String?): String? {
        if (ip == null) return null
        for (server in serverList) {
            if (ip.endsWith(server.key, ignoreCase = true)) {
                return server.value
            }
        }
        return ip
    }

    @SubscribeEvent
    fun onServerJoined(event: ClientConnectedToServerEvent) {
        if (!event.isLocal) {
            RedactionConfig.lastServerIP = Minecraft.getMinecraft().currentServerData?.serverIP ?: ""
            RedactionConfig.save()
        }
    }
}