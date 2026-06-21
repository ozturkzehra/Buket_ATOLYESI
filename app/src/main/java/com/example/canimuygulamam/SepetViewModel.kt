package com.example.canimuygulamam

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sepete eklenecek nihai buket paket modeli
data class CartItem(
    val flowerList: List<Pair<Flower, Int>>, // Seçilen çiçekler ve adetleri (Çoklu çiçek desteği)
    val cardboardColor: MaterialColor? = null,
    val tulleColor: MaterialColor? = null,
    val ribbonColor: MaterialColor? = null,
    val kartNotu: String? = null
)

class SepetViewModel : ViewModel() {
    private val _sepetList = MutableStateFlow<List<CartItem>>(emptyList())
    val sepetList = _sepetList.asStateFlow()

    // Ana ekrandan tasarım ekranına aktarılacak geçici çiçek listesi
    private val _aktifTasarimCicekleri = MutableStateFlow<List<Pair<Flower, Int>>>(emptyList())
    val aktifTasarimCicekleri = _aktifTasarimCicekleri.asStateFlow()

    // İsmi tamamen birleşik ve hatasız fonksiyon
    fun tasarimCicekleriniSetEt(liste: List<Pair<Flower, Int>>) {
        _aktifTasarimCicekleri.value = liste
    }

    fun sepeteEkle(
        flowers: List<Pair<Flower, Int>>,
        cardboard: MaterialColor? = null,
        tulle: MaterialColor? = null,
        ribbon: MaterialColor? = null,
        kartNotu: String? = null
    ) {
        val newItem = CartItem(flowers, cardboard, tulle, ribbon, kartNotu)
        _sepetList.value = _sepetList.value + newItem
    }

    fun sepettenCikar(item: CartItem) {
        _sepetList.value = _sepetList.value - item
    }
}