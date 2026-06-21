package com.example.canimuygulamam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SepetScreen(navController: NavController, sepetViewModel: SepetViewModel = viewModel()) {
    val sepetList by sepetViewModel.sepetList.collectAsState()
    
    // Toplam Fiyat Hesaplama (Fiyattaki rakamları ayıklayıp topluyoruz)
    val totalAmount = sepetList.sumOf { 
        it.flower.price.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0 
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Sepetim", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.5f),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            if (sepetList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sepetiniz Boş", style = MaterialTheme.typography.titleLarge, color = Color.Gray)
                        Button(onClick = { navController.navigate("main") }, Modifier.padding(top = 24.dp)) {
                            Text("Hemen Tasarıma Başla")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(sepetList) { item ->
                        ProfessionalSepetCard(item) {
                            sepetViewModel.sepettenCikar(item)
                        }
                    }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }

                // FİYAT ÖZETİ (Profesyonel Görünüm)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ara Toplam", color = Color.Gray)
                            Text("$totalAmount TL", fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Teslimat Ücreti", color = Color.Gray)
                            Text("Ücretsiz", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Genel Toplam", style = MaterialTheme.typography.titleLarge)
                            Text("$totalAmount TL", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        }
                        Button(
                            onClick = { /* Ödeme Adımı */ },
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Siparişi Onayla", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfessionalSepetCard(item: CartItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item.flower.imageRes),
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.flower.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = item.flower.price, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    if (item.cardboardColor != null) {
                        SmallIndicator("Karton", Color(item.cardboardColor.toColorInt()))
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    if (item.tulleColor != null) {
                        SmallIndicator("Tül", Color(item.tulleColor.toColorInt()))
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Sil", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun SmallIndicator(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color).border(0.5.dp, Color.LightGray, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SepetScreenPreview() {
    val customColorScheme = lightColorScheme(primary = Color(0xFF3D4835), surface = Color(0xFFF4F3F1))
    MaterialTheme(colorScheme = customColorScheme, typography = AppTypography) {
        SepetScreen(navController = rememberNavController())
    }
}
