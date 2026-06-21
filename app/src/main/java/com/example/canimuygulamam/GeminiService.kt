package com.example.canimuygulamam

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiService {

    private const val MODEL = "gemini-2.5-flash-lite"
    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/v1beta/models"

    suspend fun generateFlowerNote(cicekIsimleri: String, duygu: String): String =
        withContext(Dispatchers.IO) {

            val prompt = """
                Sen yaratıcı bir çiçekçi asistanısın. 
                Müşterinin buketi: $cicekIsimleri.
                Buketle iletilmek istenen temel duygu/tema: $duygu.
                
                Senden ricam, bu çiçeklerin anlamını ve temayı harmanlayan duygu yüklü, samimi bir kart notu yazman.
                
                KURALLAR:
                1. Standart, klişe ve birbirinin kopyası cümlelerden kesinlikle uzak dur.
                2. Her istek geldiğinde benzersiz bir bakış açısı yakala; bazen şiirsel, bazen kısa ve vurucu, bazen edebi bir üslup kullan.
                3. Doğrudan karta yazılacak mesajı ver, başında veya sonunda açıklama olmasın.
            """.trimIndent()

            return@withContext try {
                val apiKey = BuildConfig.GEMINI_API_KEY

                if (apiKey.isBlank()) {
                    return@withContext "API key bulunamadı. local.properties dosyasını kontrol et."
                }

                val url = URL("$BASE_URL/$MODEL:generateContent?key=$apiKey")
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 30000
                conn.readTimeout = 30000

                val body = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", prompt)
                                })
                            })
                        })
                    })

                    put("generationConfig", JSONObject().apply {
                        put("temperature", 0.95)
                    })
                }.toString()

                OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
                    writer.write(body)
                    writer.flush()
                }

                val responseCode = conn.responseCode

                val responseText = if (responseCode in 200..299) {
                    BufferedReader(InputStreamReader(conn.inputStream, "UTF-8")).use { it.readText() }
                } else {
                    BufferedReader(InputStreamReader(conn.errorStream, "UTF-8")).use { it.readText() }
                }

                if (responseCode !in 200..299) {
                    return@withContext "API Hatası ($responseCode): $responseText"
                }

                val json = JSONObject(responseText)
                json.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

            } catch (e: Exception) {
                e.printStackTrace()
                "Hata Detayı: ${e.localizedMessage ?: e.message}"
            }
        }
}