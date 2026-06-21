package com.example.canimuygulamam

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, sepetViewModel: SepetViewModel = viewModel()) {
    val workshopSteps = listOf("Ana Çiçek", "Yan Çiçek")
    var selectedCategory by remember { mutableStateOf("Ana Çiçek") }

    // Seçilen Çiçeklerin Adetlerini harita (Map) yapısında tutuyoruz: FlowerId -> Adet
    var secilenCicekAdetleri by remember { mutableStateOf(mapOf<Int, Int>()) }
    val context = LocalContext.current

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
            // Kategori Seçici
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

            // Çiçek Listesi Alanı
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredItems = flowerList.filter { it.category == selectedCategory }
                items(filteredItems) { item ->
                    val mevcutAdet = secilenCicekAdetleri[item.id] ?: 0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = null,
                                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(text = item.price, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                            }

                            // Adet Seçici Bölümü (+ / -)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (mevcutAdet > 0) {
                                    IconButton(onClick = {
                                        val yeniMap = secilenCicekAdetleri.toMutableMap()
                                        if (mevcutAdet == 1) yeniMap.remove(item.id) else yeniMap[item.id] = mevcutAdet - 1
                                        secilenCicekAdetleri = yeniMap
                                    }) {
                                        Icon(Icons.Default.Remove, contentDescription = "Azalt", tint = Color.Red)
                                    }
                                    Text(text = mevcutAdet.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }

                                IconButton(onClick = {
                                    val yeniMap = secilenCicekAdetleri.toMutableMap()
                                    yeniMap[item.id] = mevcutAdet + 1
                                    secilenCicekAdetleri = yeniMap
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Artır", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        // Alt Buton Alanı
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = {
                    val secilenListesi = flowerList.filter { secilenCicekAdetleri.containsKey(it.id) }.map {
                        Pair(it, secilenCicekAdetleri[it.id] ?: 0)
                    }

                    if (secilenListesi.isNotEmpty()) {
                        // Birleşik ismiyle çağırıyoruz, derleme hatası vermez
                        sepetViewModel.tasarimCicekleriniSetEt(secilenListesi)
                        navController.navigate("flowerDetail/studio")
                    } else {
                        Toast.makeText(context, "Lütfen en az bir çiçek adedi seçin!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Buket Tasarımına Başla", style = MaterialTheme.typography.titleMedium)
            }
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