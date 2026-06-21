package com.example.canimuygulamam

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val workshopSteps = listOf("Ana Çiçek", "Yan Çiçek", "Karton", "Tül", "Kurdele")
    var selectedCategory by remember { mutableStateOf("Ana Çiçek") }
    var selectedEmotion by remember { mutableStateOf("Tümü") }
    
    // Renk Ayarı State'leri (Karton, Tül ve Kurdele için)
    var hue by remember { mutableStateOf(0f) }
    var saturation by remember { mutableStateOf(0.6f) }
    var value by remember { mutableStateOf(0.9f) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Buket Atölyesi", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = { navController.navigate("sepet") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Sepet")
                    }
                    IconButton(onClick = { navController.navigate("profil") }) {
                        Icon(Icons.Default.Person, contentDescription = "Profil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.5f))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            // 1. ADIM SEÇİCİ (Üst Sekmeler)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(workshopSteps) { step ->
                    FilterChip(
                        selected = selectedCategory == step,
                        onClick = { selectedCategory = step },
                        label = { Text(step) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // 2. İÇERİK ALANI
            if (selectedCategory == "Karton" || selectedCategory == "Tül" || selectedCategory == "Kurdele") {
                // SADECE MALZEMELERDE GELİŞMİŞ RENK ÇARKINI GÖSTER
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$selectedCategory Rengini Ayarla", 
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    ColorWheelPicker(
                        hue = hue,
                        onHueChange = { hue = it }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    SliderRow("Doygunluk", saturation) { saturation = it }
                    SliderRow("Parlaklık", value) { value = it }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Seçilen rengin önizlemesi (Küçük bir kutu)
                    Surface(
                        modifier = Modifier.size(100.dp, 40.dp),
                        color = Color.hsv(hue, saturation, value),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                        shadowElevation = 4.dp
                    ) {}
                }
            } else {
                // ÇİÇEKLERDE ESKİ TEMİZ LİSTE HALİ
                FlowerSection(
                    selectedCategory = selectedCategory,
                    selectedEmotion = selectedEmotion,
                    onEmotionSelect = { selectedEmotion = it },
                    navController = navController
                )
            }
        }

        // Alt Buton
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = { /* Seçimi Onayla ve Sepete Ekle */ },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Buketi Sepete Ekle", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun ColorWheelPicker(hue: Float, onHueChange: (Float) -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .size(220.dp)
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
            val thickness = 30.dp.toPx()

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
            
            drawCircle(color = Color.White, radius = 12.dp.toPx(), center = Offset(selectorX, selectorY), style = Stroke(width = 3.dp.toPx()))
            drawCircle(color = Color.hsv(hue, 1f, 1f), radius = 8.dp.toPx(), center = Offset(selectorX, selectorY))
        }
    }
}

@Composable
fun SliderRow(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Slider(value = value, onValueChange = onValueChange, valueRange = 0f..1f)
    }
}

@Composable
fun FlowerSection(selectedCategory: String, selectedEmotion: String, onEmotionSelect: (String) -> Unit, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(emotions) { emotion ->
                AssistChip(
                    onClick = { onEmotionSelect(emotion) }, 
                    label = { Text(emotion) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selectedEmotion == emotion) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) 
                        else Color.White.copy(alpha = 0.6f)
                    )
                )
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val filteredItems = flowerList.filter { it.category == selectedCategory && (selectedEmotion == "Tümü" || it.emotion == selectedEmotion) }
            items(filteredItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("flowerDetail/${item.id}") },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                ) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = item.imageRes), contentDescription = item.name, modifier = Modifier.size(70.dp).clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop)
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(text = item.price, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                        }
                        Icon(Icons.Default.AddCircle, contentDescription = "Ekle", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val customColorScheme = lightColorScheme(primary = Color(0xFF3D4835), surface = Color(0xFFF4F3F1))
    MaterialTheme(colorScheme = customColorScheme, typography = AppTypography) {
        MainScreen(navController = rememberNavController())
    }
}
