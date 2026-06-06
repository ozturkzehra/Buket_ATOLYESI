package com.example.canimuygulamam

// Sepet öğesi data class
data class SepetItem(
    val ad: String,
    val fiyat: String,
    val drawable: String
)

// Global sepet yöneticisi (singleton)
object SepetManager {
    private val sepet = mutableListOf<SepetItem>()

    fun sepeteEkle(item: SepetItem) {
        sepet.add(item)
    }

    fun sepettenCikar(index: Int) {
        if (index in sepet.indices) sepet.removeAt(index)
    }

    fun getSepet(): List<SepetItem> = sepet.toList()

    fun sepetiBosalt() {
        sepet.clear()
    }

    fun toplamFiyat(): Int {
        return sepet.sumOf { item ->
            item.fiyat.replace(" TL", "").replace(".", "").toIntOrNull() ?: 0
        }
    }
}
