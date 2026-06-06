package com.example.canimuygulamam

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SepetActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var txtBos: TextView
    private lateinit var txtToplamFiyat: TextView
    private lateinit var btnBuketOlustur: Button
    private lateinit var layoutBuketPrompt: View
    private lateinit var editPrompt: EditText
    private lateinit var btnPromptGonder: Button
    private lateinit var txtBuketSonuc: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSepetiBosalt: Button

    // =========================================================================
    // Apı keyimi ekledim
    private val GEMINI_API_KEY = "AQ.Ab8RN6IGFKn-dYFby3rVbuvlo-oCMdN35EfkmH339gro8DSB_w"
    // =========================================================================

    private lateinit var adapter: SepetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sepet)

        tabLayout         = findViewById(R.id.tabLayout)
        recyclerView      = findViewById(R.id.recyclerViewSepet)
        txtBos            = findViewById(R.id.txtSepetBos)
        txtToplamFiyat    = findViewById(R.id.txtToplamFiyat)
        btnBuketOlustur   = findViewById(R.id.btnBuketOlustur)
        layoutBuketPrompt = findViewById(R.id.layoutBuketPrompt)
        editPrompt        = findViewById(R.id.editPrompt)
        btnPromptGonder   = findViewById(R.id.btnPromptGonder)
        txtBuketSonuc     = findViewById(R.id.txtBuketSonuc)
        progressBar       = findViewById(R.id.progressBar)
        btnSepetiBosalt   = findViewById(R.id.btnSepetiBosalt)

        setupTabs()
        setupRecyclerView()
        updateUI()

        //TEK TEK CİCEKLERİ EKLEYİP METİN YAZIYORUM
        btnBuketOlustur.setOnClickListener {
            if (SepetManager.getSepet().isEmpty()) {
                Toast.makeText(this, "Sepetiniz bos! Once cicek ekleyin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            layoutBuketPrompt.visibility = View.VISIBLE
            val cicekListesi = SepetManager.getSepet().joinToString(", ") { it.ad }
            editPrompt.setText("$cicekListesi iceren zarif bir buket. Romantik ve sik gorunum.")
        }

        btnPromptGonder.setOnClickListener {
            val prompt = editPrompt.text.toString().trim()
            if (prompt.isEmpty()) {
                Toast.makeText(this, "Lutfen bir aciklama girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buketKartiOlustur(prompt)
        }

        btnSepetiBosalt.setOnClickListener {
            SepetManager.sepetiBosalt()
            txtBuketSonuc.text = ""
            layoutBuketPrompt.visibility = View.GONE
            updateUI()
            Toast.makeText(this, "Sepet bosaltildi.", Toast.LENGTH_SHORT).show()
        }
    }

    //alttakı cicekler ve ana sayfa
    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Ana Sayfa"))
        tabLayout.addTab(tabLayout.newTab().setText("Cicekler"))
        tabLayout.addTab(tabLayout.newTab().setText("Sepet"))
        tabLayout.getTabAt(2)?.select()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> { startActivity(Intent(this@SepetActivity, MainActivity::class.java)); finish() }
                    1 -> { startActivity(Intent(this@SepetActivity, CiceklerActivity::class.java)); finish() }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    //urunleri listeliyor ve silinen urunlerı cıkarıyor
    private fun setupRecyclerView() {
        adapter = SepetAdapter(SepetManager.getSepet().toMutableList()) { index ->
            SepetManager.sepettenCikar(index)
            updateUI()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    //sepetim kendini yeniliyor
   // Bu fonksiyon sepetin durumuna göre
    //ekranı anlık olarak değiştirir.
    //Eğer sepet boşsa listeyi gizleyip
    //"Sepetiniz boş" yazısını (txtBos) açar ve yapay zeka butonunu kilitler
    private fun updateUI() {
        val sepet = SepetManager.getSepet()
        adapter.updateList(sepet.toMutableList())
        if (sepet.isEmpty()) {
            txtBos.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            btnBuketOlustur.isEnabled = false
            txtToplamFiyat.text = "0 TL"
        } else {
            txtBos.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            btnBuketOlustur.isEnabled = true
            txtToplamFiyat.text = "${SepetManager.toplamFiyat()} TL"
        }
    }

    private fun buketKartiOlustur(userPrompt: String) {
        progressBar.visibility = View.VISIBLE
        btnPromptGonder.isEnabled = false
        txtBuketSonuc.text = ""

        val cicekListesi = SepetManager.getSepet().joinToString(", ") { it.ad }

        val fullPrompt = """
Sen bir çiçekçi dükkanının yapay zeka asistanısın. 
Müşteri sepetine şu çiçekleri ekledi: $cicekListesi

Müşterinin isteği: $userPrompt

Bu çiçeklerle hazırlanacak buket için şunları yaz:

 BUKET ADI: (yaratıcı bir isim)

 BUKET KARTI YAZISI: (kişiye özel, şiirsel ve duygusal 3-4 cümlelik bir kart yazısı)

 BUKET HAKKINDA: (bu çiçeklerin anlamı, renk uyumu ve özel günler için neden ideal olduğu - 2-3 cümle)

 AMBALAJ ÖNERİSİ: (buketle uyumlu ambalaj ve sunum önerisi - 1-2 cümle)

Türkçe yaz, samimi ve şiirsel bir dil kullan.
        """.trimIndent()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = callGeminiAPI(fullPrompt)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    btnPromptGonder.isEnabled = true
                    txtBuketSonuc.text = response
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    btnPromptGonder.isEnabled = true
                    txtBuketSonuc.text = "Hata olustu: ${e.message}\n\nEger hata 401 ise lütfen adımları kontrol edin."
                }
            }
        }
    }

    private fun callGeminiAPI(prompt: String): String {
        // En kararlı v1 ana sürüm endpoint'ine yönlendirildi
        val url = URL("https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=$GEMINI_API_KEY")
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
            throw Exception("API Hatasi ($responseCode). Detay: $sb")
        }

        val json = JSONObject(sb.toString())
        return json
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
    }
}

class SepetAdapter(
    private var items: MutableList<SepetItem>,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<SepetAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val txtAd: TextView = view.findViewById(R.id.txtSepetItemAd)
        val txtFiyat: TextView = view.findViewById(R.id.txtSepetItemFiyat)
        val btnSil: ImageButton = view.findViewById(R.id.btnSepetItemSil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sepet, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.txtAd.text = item.ad
        holder.txtFiyat.text = item.fiyat
        holder.btnSil.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_ID.toInt()) onRemove(pos)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: MutableList<SepetItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}