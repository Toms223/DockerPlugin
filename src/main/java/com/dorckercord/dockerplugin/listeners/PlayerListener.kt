package com.dorckercord.dockerplugin.listeners

import com.dorckercord.dockerplugin.requests.DockerCordAPIClient
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.net.InetSocketAddress
import java.util.*
import java.util.logging.Logger

class PlayerListener(private val proxy: ProxyServer): Listener {
    companion object{
        val dockerClient = DockerCordAPIClient("dockercord:3030")
    }
    @EventHandler
    fun onPlayerJoin(event: PostLoginEvent){
        val playerUUID = event.player.uniqueId
        if(!checkPlayerExists(playerUUID)){
            dockerClient.registerPlayer(playerUUID)
        }
        if(!checkInstanceExists(playerUUID)){
            dockerClient.createInstance(playerUUID)
        }
        val instance = dockerClient.getInstance(playerUUID) ?: throw ClassNotFoundException()
        if(!instance.running){
            dockerClient.startInstance(instance)
        }
        val address = InetSocketAddress(instance.playerUUID.toString(),25565)
        val serverInfo = proxy.constructServerInfo(
            instance.playerUUID.toString(),
            address,
            "${event.player.name}'s Server",
            false)
        proxy.servers[instance.playerUUID.toString()] = serverInfo
        event.player.connect(serverInfo)
    }

    private fun checkInstanceExists(playerUUID: UUID): Boolean{
        return dockerClient.getInstance(playerUUID) != null
    }

    private fun checkPlayerExists(playerUUID: UUID): Boolean {
        return dockerClient.getPlayer(playerUUID) != null
    }
}