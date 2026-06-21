package com.example.canimuygulamam

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch // Hata almamak için coroutine importunu ekledik
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerDetailScreen(
    navController: NavController,
    sepetViewModel: SepetViewModel = viewModel()
) {
    val secilenCicekler by sepetViewModel.aktifTasarimCicekleri.collectAsState()
    val context = LocalContext.current

    // Yapay Zeka İstek Yönetimi İçin State'ler 🛠️
    val coroutineScope = rememberCoroutineScope()
    var isAiLoading by remember { mutableStateOf(false) }

    // Malzemeler için dinamik HSV Tabanlı Renk State'leri
    var kartonHue by remember { mutableStateOf(30f) }
    var kartonSat by remember { mutableStateOf(0.4f) }
    var kartonVal by remember { mutableStateOf(0.8f) }

    var tulHue by remember { mutableStateOf(0f) }
    var tulSat by remember { mutableStateOf(0.0f) }
    var tulVal by remember { mutableStateOf(1.0f) }

    var kurdeleHue by remember { mutableStateOf(0f) }
    var kurdeleSat by remember { mutableStateOf(1.0f) }
    var kurdeleVal by remember { mutableStateOf(0.5f) }

    var kartNotu by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Buketini Tasarla", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.5f))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Seçilen Çiçeklerin Özeti
            Text(text = "Buket İçeriği", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            secilenCicekler.forEach { pair ->
                val flower = pair.first
                val adet = pair.second
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f))
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = flower.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "${flower.name} x$adet", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // 2. KARTON RENK SEÇİM ALANI
            Text(text = "Karton Rengini Parmağınla Ayarla", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorWheelStudio(hue = kartonHue, onHueChange = { kartonHue = it })

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Önizleme", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(70.dp, 50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.hsv(kartonHue, kartonSat, kartonVal))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    )
                }
            }
            ColorSliderRow(label = "Karton Doygunluğu", value = kartonSat, onValueChange = { kartonSat = it })
            ColorSliderRow(label = "Karton Parlaklığı", value = kartonVal, onValueChange = { kartonVal = it })

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

            // 3. TÜL RENK SEÇİM ALANI
            Text(text = "Tül Rengini Parmağınla Ayarla", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorWheelStudio(hue = tulHue, onHueChange = { tulHue = it })

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Önizleme", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(70.dp, 50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.hsv(tulHue, tulSat, tulVal))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    )
                }
            }
            ColorSliderRow(label = "Tül Doygunluğu", value = tulSat, onValueChange = { tulSat = it })
            ColorSliderRow(label = "Tül Parlaklığı", value = tulVal, onValueChange = { tulVal = it })

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

            // 4. KURDELE RENK SEÇİM ALANI
            Text(text = "Kurdele Rengini Parmağınla Ayarla", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorWheelStudio(hue = kurdeleHue, onHueChange = { kurdeleHue = it })

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Önizleme", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(70.dp, 50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.hsv(kurdeleHue, kurdeleSat, kurdeleVal))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    )
                }
            }
            ColorSliderRow(label = "Kurdele Doygunluğu", value = kurdeleSat, onValueChange = { kurdeleSat = it })
            ColorSliderRow(label = "Kurdele Parlaklığı", value = kurdeleVal, onValueChange = { kurdeleVal = it })

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // 5. GERÇEK GEMINI API ENTEGRASYONLU KART NOTU ALANI 🚀
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Buket Kart Notu", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                TextButton(
                    enabled = !isAiLoading,
                    onClick = {
                        val cicekIsimleri = secilenCicekler.joinToString(", ") { it.first.name }
                        val dominantEmotion = secilenCicekler.firstOrNull()?.first?.emotion ?: "Genel"

                        isAiLoading = true
                        coroutineScope.launch {
                            // GeminiService sınıfındaki gerçek yapay zekayı tetikliyoruz
                            val aiNote = GeminiService.generateFlowerNote(cicekIsimleri, dominantEmotion)
                            kartNotu = aiNote
                            isAiLoading = false
                            Toast.makeText(context, "Yapay zeka notunu hazırladı! ✨", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    if (isAiLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI Düşünüyor...")
                    } else {
                        Icon(Icons.Default.Create, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI ile Not Yaz")
                    }
                }
            }

            OutlinedTextField(
                value = kartNotu,
                onValueChange = { kartNotu = it },
                placeholder = { Text("Mesajınızı buraya yazın...") },
                modifier = Modifier.fillMaxWidth().height(90.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // 6. Sepete Ekleme Tetikleyicisi
            Button(
                onClick = {
                    val finalKarton = MaterialColor("Özel Karton", Color.hsv(kartonHue, kartonSat, kartonVal), "0 TL")
                    val finalTul = MaterialColor("Özel Tül", Color.hsv(tulHue, tulSat, tulVal), "0 TL")
                    val finalKurdele = MaterialColor("Özel Kurdele", Color.hsv(kurdeleHue, kurdeleSat, kurdeleVal), "0 TL")

                    sepetViewModel.sepeteEkle(
                        flowers = secilenCicekler,
                        cardboard = finalKarton,
                        tulle = finalTul,
                        ribbon = finalKurdele,
                        kartNotu = kartNotu.ifBlank { "Yok" }
                    )
                    Toast.makeText(context, "Özel tasarım buketiniz sepete eklendi!", Toast.LENGTH_SHORT).show()
                    navController.navigate("sepet") { popUpTo("main") }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Tasarımı Sepete Ekle", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun ColorWheelStudio(hue: Float, onHueChange: (Float) -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .size(140.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val angle = atan2(change.position.y - center.y, change.position.x - center.x)
                        val newHue = (Math.toDegrees(angle.toDouble()).toFloat() + 360f) % 360f
                        onHueChange(newHue)
                    }
                }
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f
            val thickness = 20.dp.toPx()

            for (i in 0 until 360) {
                drawArc(
                    color = Color.hsv(i.toFloat(), 1f, 1f),
                    startAngle = i.toFloat(),
                    sweepAngle = 2f,
                    useCenter = false,
                    style = Stroke(width = thickness)
                )
            }

            val selectorRadius = radius - (thickness / 2f)
            val selectorX = center.x + selectorRadius * cos(Math.toRadians(hue.toDouble())).toFloat()
            val selectorY = center.y + selectorRadius * sin(Math.toRadians(hue.toDouble())).toFloat()

            drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(selectorX, selectorY), style = Stroke(width = 2.5.dp.toPx()))
            drawCircle(color = Color.hsv(hue, 1f, 1f), radius = 5.dp.toPx(), center = Offset(selectorX, selectorY))
        }
    }
}

@Composable
fun ColorSliderRow(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        )
    }
}