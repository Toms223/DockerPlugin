package com.dorckercord.dockerplugin.listeners

import com.dorckercord.dockerplugin.requests.DockerCordAPIClient
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.net.InetSocketAddress
import java.util.logging.Level
import java.util.logging.Logger

class PlayerListener(private val logger: Logger, private val proxy: ProxyServer): Listener {
    companion object{
        val dockerClient = DockerCordAPIClient("localhost:3030")
    }
    @EventHandler
    fun onPlayerLogin(event: PostLoginEvent){
        val playerUUID = event.player.uniqueId
        if(dockerClient.players.none { uuid -> uuid == playerUUID }){
            dockerClient.registerPlayer(playerUUID)
            val instance = dockerClient.createAndStartInstance(playerUUID)
            val serverInfo = proxy.constructServerInfo("$playerUUID", InetSocketAddress(instance.playerUUID.toString(),25565),"", false)
            proxy.servers["$playerUUID"] = serverInfo
            event.player.connect(proxy.servers["$playerUUID"])
        }
    }
}