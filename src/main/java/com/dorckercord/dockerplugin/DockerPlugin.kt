package com.dorckercord.dockerplugin

import com.dorckercord.dockerplugin.listeners.PlayerListener
import net.md_5.bungee.api.plugin.Plugin

class DockerPlugin: Plugin() {
    override fun onEnable() {
        proxy.pluginManager.registerListener(this,PlayerListener(logger,proxy))
    }
}