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
    Flower(1, "Antoryum", "50 TL", "Kalp şeklindeki parlak yapraklarıyla sıcak bir karşılamanın ve samimiyetin simgesidir.", "Seni seviyorum.", R.drawable.antoryum, "Ana Çiçek", "Flower", "Aşk"),
    Flower(2, "Beyaz Karanfil", "45 TL", "Dürüstlüğün ve lekesiz bir sevginin sembolüdür.", "Masumiyet.", R.drawable.beyaz_karanfil, "Ana Çiçek", "Flower", "Masumiyet"),
    Flower(3, "Beyaz Lale", "300 TL", "Affet beni demenin en zarif ve duru yoludur.", "Eşsizsin.", R.drawable.beyaz_lale, "Ana Çiçek", "Flower", "Tebrik"),
    Flower(4, "Beyaz Orkide", "40 TL", "Temiz bir başlangıcın ve sarsılmaz bir bağlılığın simgesidir.", "Hayranlık.", R.drawable.beyaz_orkide, "Ana Çiçek", "Flower", "Dostluk"),
    Flower(5, "Guzmanya", "20 TL", "Girdiği her mekana şans ve maddi-manevi bereket getirdiğine inanılan tropikal bir mucizedir.", "Temiz sevgi.", R.drawable.guzmanya, "Yan Çiçek", "Flower", "Masumiyet"),
    Flower(6, "Kalanso", "30 TL", "Zorluklara karşı dirençli olmayı ve tükenmeyen bir sevgiyi temsil eder.", "Zarafet.", R.drawable.kalanso, "Yan Çiçek", "Flower", "Zarafet"),
    Flower(7, "Kasımpatı", "25 TL", "Sonbaharın en asil çiçeği olan Kasımpatı, zorlu hava koşullarına direnciyle sarsılmaz bir sadakati ve vefayı temsil eder.", "Tazelik.", R.drawable.kasimpati, "Yan Çiçek", "Flower", "Sadakat"),
    Flower(8, "Kırmızı Karanfil", "45 TL", "Derin bir sevginin ve sarsılmaz bir bağlılığın sembolüdür.", "Masumiyet.", R.drawable.kirmizi_karanfil, "Ana Çiçek", "Flower", "Masumiyet"),
    Flower(9, "Kırmızı Lale", "300 TL", "Osmanlı'dan günümüze doğrudan aşk ilan etmenin en asil ve köklü yoludur.", "Eşsizsin.", R.drawable.kirmizi_lale, "Ana Çiçek", "Flower", "Tebrik"),
    Flower(10, "Kokina", "40 TL", "Kırmızı meyveleriyle kış aylarının ve yılbaşı ruhunun vazgeçilmezi olan Kokina, şans ve yeni umutların simgesidir.", "Hayranlık.", R.drawable.kokina, "Ana Çiçek", "Flower", "Dostluk"),
    Flower(11, "Lilyum", "20 TL", "Kadim kültürlerde ruhsal arınmanın, dürüstlüğün ve yeniden doğuşun sembolüdür.", "Temiz sevgi.", R.drawable.lilyum, "Yan Çiçek", "Flower", "Masumiyet"),
    Flower(12, "Mavi Orkide", "30 TL", "Doğanın büyüleyici gizemini yansıtan mavi orkide, ulaşılamaz olanı ve sonsuzluğu simgeler.", "Zarafet.", R.drawable.mavi_orkide, "Yan Çiçek", "Flower", "Zarafet"),
    Flower(13, "Menekse", "25 TL", "Gösterişten uzak ama derin bir sadakatin simgesidir.", "Tazelik.", R.drawable.menekse, "Yan Çiçek", "Flower", "Sadakat"),
    Flower(14, "Mor Lilyum", "50 TL", "Asaletin, ihtişamın ve sıra dışı bir başarının sembolüdür.", "Seni seviyorum.", R.drawable.mor_lilyum, "Ana Çiçek", "Flower", "Aşk"),
    Flower(15, "Mor Orkide", "45 TL", "Sana derin bir saygı ve hayranlık duyuyorum demenin en asil yoludur.", "Masumiyet.", R.drawable.mor_orkide, "Ana Çiçek", "Flower", "Masumiyet"),
    Flower(16, "Nergis", "300 TL", "Kışın ardından gelen baharın ilk müjdecisidir; özgüveni, yenilenmeyi ve umudu simgeler.", "Eşsizsin.", R.drawable.nergis, "Ana Çiçek", "Flower", "Tebrik"),
    Flower(17, "Papatya", "40 TL", "Çocuksu bir masumiyetin, temiz duyguların ve sarsılmaz bir sadakatin en doğal temsilcisidir.", "Hayranlık.", R.drawable.papatya, "Ana Çiçek", "Flower", "Dostluk"),
    Flower(18, "Pembe Karanfil", "20 TL", "Seni asla unutmayacağım mesajını taşıyan, minnet dolu bir kalbin simgesidir.", "Temiz sevgi.", R.drawable.pembe_karanfil, "Yan Çiçek", "Flower", "Masumiyet"),
    Flower(19, "Pembe Lale", "30 TL", "Zarafetin, nezaketin ve sevdiklerinize duyduğunuz iyi dileklerin simgesidir.", "Zarafet.", R.drawable.pembe_lale, "Yan Çiçek", "Flower", "Zarafet"),
    Flower(20, "Pembe Lilyum", "25 TL", "Zarafeti ve nezaketi temsil ederek 'Sana hayranım' demenin en nazik ve şefkatli yoludur.", "Tazelik.", R.drawable.pembe_lilyum, "Yan Çiçek", "Flower", "Zarafet"),
    Flower(21, "Pembe Papatya", "45 TL", "Sevginin en nazik, korumacı ve şefkat dolu halini yansıtır.", "Masumiyet.", R.drawable.pembe_papatya, "Ana Çiçek", "Flower", "Masumiyet"),
    Flower(22, "Sakayik", "300 TL", "Kat kat yapraklarıyla görkemli bir güzelliğe, refaha ve mutlu bir yaşama işaret eder.", "Eşsizsin.", R.drawable.sakayik, "Ana Çiçek", "Flower", "Tebrik"),
    Flower(23, "Sarı Karanfil", "40 TL", "Duyguların en hassas ve düşünceli halini temsil eder.", "Hayranlık.", R.drawable.sari_karanfil, "Ana Çiçek", "Flower", "Dostluk"),
    Flower(24, "Sarı Papatya", "30 TL", "Günü aydınlatan sarı papatyalar neşenin, sarsılmaz dostluğun ve iyimserliğin simgesidir.", "Zarafet.", R.drawable.sari_papatya, "Yan Çiçek", "Flower", "Dostluk"),
    Flower(25, "Spatifilyum", "25 TL", "Barış çiçeği veya yelken çiçeği olarak bilinir; huzuru ve arınmayı simgeler.", "Ferahlık.", R.drawable.spatifiyum, "Yan Çiçek", "Flower", "Huzur")
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