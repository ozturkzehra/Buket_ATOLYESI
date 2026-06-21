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

    // Diğer sayfada çalışan, senin o çalışan gerçek API anahtarın
    private const val GIZLI_ANAHTAR = "AQ.Ab8RN6Klpx8-mRet5KL7zW0Hz74Mo7gslGaXuukkD2e1LEYK4Q"

    suspend fun generateFlowerNote(cicekIsimleri: String, duygu: String): String = withContext(Dispatchers.IO) {
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
            // Önceki projende çalışan v1beta endpoint'i ve modeli el ile çağırıyoruz
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=$GIZLI_ANAHTAR")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 30000
            conn.readTimeout = 30000

            // Her seferinde farklı yazması için %95 yaratıcılık (temperature) ayarını JSON'a ekledik ⚙️
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
                // Yaratıcılık ayarı buraya gömüldü
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.95)
                })
            }.toString()

            val writer = OutputStreamWriter(conn.outputStream, "UTF-8")
            writer.write(body)
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            val reader = if (responseCode == 200) {
                BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
            } else {
                BufferedReader(InputStreamReader(conn.errorStream, "UTF-8"))
            }

            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            reader.close()

            if (responseCode != 200) {
                throw Exception("API Hatası ($responseCode). Detay: $sb")
            }

            // JSON verisini ayıklayıp sadece gelen metni (text) alıyoruz
            val json = JSONObject(sb.toString())
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