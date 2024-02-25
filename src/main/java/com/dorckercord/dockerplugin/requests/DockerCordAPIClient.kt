package com.dorckercord.dockerplugin.requests

import com.dorckercord.dockerplugin.domain.Instance
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.IllegalStateException
import java.util.*

class DockerCordAPIClient(private val host: String) {
    companion object{
        enum class Method{
            GET,
            PUT,
            DELETE,
            POST
        }
        private val client = OkHttpClient()
        fun jsonRequest(host: String, path: String, method: Method,vararg parameters: String): JsonElement{
            val url = "http://$host/$path" + (parameters.joinToString { "/$it" })
            val requestBody: RequestBody? = if(method == Method.GET) null else  "".toRequestBody()
            val request = Request.Builder()
                .url(url)
                .method(method.name,requestBody).build()
            val body = client.newCall(request).execute().body?.string() ?: throw IOException()
            return JsonParser.parseString(body)
        }
    }

    fun createInstance(playerUUID: UUID): Instance {
        val response = jsonRequest(host,"instances/create",Method.PUT,playerUUID.toString()).asJsonObject
        return Instance.jsonToInstance(response)
    }

    fun startInstance(instance: Instance): Instance {
        val response = jsonRequest(host,"instances/start",Method.PUT,instance.playerUUID.toString()).asJsonObject
        return Instance.jsonToInstance(response)
    }

    fun getInstance(playerUUID: UUID): Instance? {
        val response = jsonRequest(host, "instances/instance",Method.GET,playerUUID.toString()).asJsonObject
        if(response["status"] != null){
            return null
        }
        return Instance.jsonToInstance(response)
    }

    fun getPlayer(playerUUID: UUID): UUID? {
        val response = jsonRequest(host, "players/player",Method.GET,playerUUID.toString()).asJsonObject
        if(response["status"] != null){
            return null
        }
        return UUID.fromString(response.asJsonObject["uuid"].asString)
    }

    fun registerPlayer(playerUUID: UUID): Boolean {
        val uuid = jsonRequest(host,"players/register",Method.PUT,playerUUID.toString()).asJsonObject["uuid"]?.asString ?: return false
        if(uuid != playerUUID.toString()){
            throw IllegalStateException("Registered player UUID doesn't match passed UUID")
        }
        return true
    }
}