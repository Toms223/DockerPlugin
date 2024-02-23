package com.dorckercord.dockerplugin.domain

import com.google.gson.JsonObject
import java.time.Instant
import java.util.*

data class Instance(val id: String, val playerUUID: UUID, val lastOnline: Instant, val running: Boolean) {
    companion object {
        fun jsonToInstance(jsonObject: JsonObject): Instance{
            val id = jsonObject["id"].asString
            val uuid = UUID.fromString(jsonObject["playerUUID"].asString)
            val lastOnline = Instant.ofEpochMilli(jsonObject["last-online"].asLong)
            val running = jsonObject["running"].asBoolean
            return Instance(id,uuid,lastOnline,running)
        }
    }
}