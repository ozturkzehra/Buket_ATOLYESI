package com.example.canimuygulamam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.canimuygulamam.databinding.CiceklerBinding
import com.google.android.material.tabs.TabLayout

class CiceklerActivity : AppCompatActivity() {
    private lateinit var binding: CiceklerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CiceklerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabs()
        setupSepetButtons()
    }

    private fun setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ana Sayfa"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Çiçekler"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("🛒 Sepet"))

        // Çiçekler sayfasında olduğumuz için 1. sekme seçili görünsün
        binding.tabLayout.getTabAt(1)?.select()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val intent = Intent(this@CiceklerActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    2 -> {
                        val intent = Intent(this@CiceklerActivity, SepetActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupSepetButtons() {
        // Çiçek adı ve fiyat eşleştirmeleri
        val cicekMap = mapOf(
            "btnSepet1" to Pair("Kırmızı Gül", "450 TL"),
            "btnSepet2" to Pair("Beyaz Papatya", "250 TL"),
            "btnSepet3" to Pair("Lale", "300 TL"),
            "btnSepet5" to Pair("Ayçiçeği", "200 TL"),
            "btnSepet6" to Pair("Orkide", "550 TL"),
            "btnSepet7" to Pair("Lavanta", "180 TL"),
            "btnSepet8" to Pair("Papatya Buketi", "350 TL")
        )

        binding.btnSepet1.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Kırmızı Gül", "450 TL", "cicek1"))
            Toast.makeText(this, "🌹 Kırmızı Gül sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSepet2.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Beyaz Papatya", "250 TL", "cicek2"))
            Toast.makeText(this, "🌼 Beyaz Papatya sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSepet3.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Lale", "300 TL", "cicek3"))
            Toast.makeText(this, "🌷 Lale sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSepet5.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Ayçiçeği", "200 TL", "cicek4"))
            Toast.makeText(this, "🌻 Ayçiçeği sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSepet6.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Orkide", "550 TL", "cicek5"))
            Toast.makeText(this, "🌸 Orkide sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSepet7.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Lavanta", "180 TL", "cicek6"))
            Toast.makeText(this, "💜 Lavanta sepete eklendi!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSepet8.setOnClickListener {
            SepetManager.sepeteEkle(SepetItem("Papatya Buketi", "350 TL", "cicek9"))
            Toast.makeText(this, "💐 Papatya Buketi sepete eklendi!", Toast.LENGTH_SHORT).show()
        }
    }
}
