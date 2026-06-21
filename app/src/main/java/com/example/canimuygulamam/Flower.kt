package com.example.canimuygulamam

import androidx.compose.ui.graphics.Color

data class Flower(
    val id: Int,
    val name: String,
    val price: String,
    val description: String,
    val meaning: String,
    val imageRes: Int,
    val category: String,
    val type: String,
    val emotion: String = ""
)

data class MaterialColor(
    val name: String,
    val color: Color,
    val price: String
)

val flowerList = listOf(
    Flower(1, "Kırmızı Gül", "50 TL", "Aşkın sembolü.", "Seni seviyorum.", R.drawable.cicek1, "Ana Çiçek", "Flower", "Aşk"),
    Flower(2, "Beyaz Gül", "45 TL", "Saflığın sembolü.", "Masumiyet.", R.drawable.buket2, "Ana Çiçek", "Flower", "Masumiyet"),
    Flower(3, "Mavi Orkide", "300 TL", "Zarafet.", "Eşsizsin.", R.drawable.cicek3, "Ana Çiçek", "Flower", "Tebrik"),
    Flower(4, "Ayçiçeği", "40 TL", "Sıcaklık.", "Hayranlık.", R.drawable.cicek9, "Ana Çiçek", "Flower", "Dostluk"),
    Flower(5, "Papatya", "20 TL", "Doğallık.", "Temiz sevgi.", R.drawable.cicek2, "Yan Çiçek", "Flower", "Masumiyet"),
    Flower(6, "Cipso", "30 TL", "Dolgunluk katar.", "Zarafet.", R.drawable.cicek5, "Yan Çiçek", "Flower"),
    Flower(7, "Okaliptüs", "25 TL", "Ferahlık.", "Tazelik.", R.drawable.bahce, "Yan Çiçek", "Flower")
)

// Zengin Renk Paleti
val materialColors = listOf(
    MaterialColor("Kraft", Color(0xFFC4A484), "20 TL"),
    MaterialColor("Pudra", Color(0xFFF1C0C0), "25 TL"),
    MaterialColor("Lila", Color(0xFFE6E6FA), "25 TL"),
    MaterialColor("Zümrüt", Color(0xFF3D4835), "30 TL"),
    MaterialColor("Gece Mavisi", Color(0xFF1A237E), "30 TL"),
    MaterialColor("Hardal", Color(0xFFFFB300), "25 TL"),
    MaterialColor("Gül Kurusu", Color(0xFF997476), "30 TL"),
    MaterialColor("Su Yeşili", Color(0xFFA2D2BB), "25 TL"),
    MaterialColor("Siyah Mat", Color(0xFF2B2B2B), "25 TL")
)

val workshopCategories = listOf("Ana Çiçek", "Yan Çiçek", "Karton", "Tül", "Kurdele")
val emotions = listOf("Tümü", "Aşk", "Masumiyet", "Tebrik", "Dostluk")
