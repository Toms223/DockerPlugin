package com.dorckercord.dockerplugin.requests

import com.dorckercord.dockerplugin.domain.Instance
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.*
import java.io.IOException
import java.util.*

class DockerCordAPIClient(private val host: String) {
    companion object{
        private val client = OkHttpClient()
        fun jsonGetRequest(host: String, path: String): JsonElement {
            val request = Request.Builder()
                .url("http://$host/$path")
                .method("GET",null).build()
            val body = client.newCall(request).execute().body?.string() ?: throw IOException()
            return JsonParser.parseString(body)
        }
    }

    val players = run {
        val response = jsonGetRequest(host, "players").asJsonArray
        response.map {user ->
            UUID.fromString(user.asJsonObject["uuid"].asString)
        }
    }

    val instances = run {
        val response = jsonGetRequest(host, "instances").asJsonArray
        response.map { instance ->
            Instance.jsonToInstance(instance.asJsonObject)
        }
    }

    fun createAndStartInstance(playerUUID: UUID): Instance {

    }

    fun registerPlayer(playerUUID: UUID) {

    }
}