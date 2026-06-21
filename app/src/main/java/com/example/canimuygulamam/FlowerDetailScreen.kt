package com.example.canimuygulamam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerDetailScreen(navController: NavController, flowerId: String?) {
    val flower = flowerList.find { it.id == flowerId?.toIntOrNull() }
    var showMeaning by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(flower?.name ?: "Çiçek Detayı", style = MaterialTheme.typography.headlineMedium) },
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
                .background(Color.White.copy(alpha = 0.2f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (flower != null) {
                Image(
                    painter = painterResource(id = flower.imageRes),
                    contentDescription = flower.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = flower.name, style = MaterialTheme.typography.headlineMedium)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = flower.price, 
                    style = MaterialTheme.typography.titleLarge, 
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // İncele (Anlamı Gör) Butonu
                TextButton(onClick = { showMeaning = !showMeaning }) {
                    Text(
                        text = if (showMeaning) "Anlamı Gizle" else "Çiçek Anlamını İncele",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // İtalik Çiçek Anlamı
                if (showMeaning) {
                    Text(
                        text = flower.meaning,
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = flower.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = { /* Sepete ekle mantığı */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sepete Ekle", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                Text("Çiçek bulunamadı.")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FlowerDetailScreenPreview() {
    val customColorScheme = lightColorScheme(
        primary = Color(0xFF3D4835),
        surface = Color(0xFFF4F3F1)
    )

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = AppTypography
    ) {
        FlowerDetailScreen(navController = rememberNavController(), flowerId = "1")
    }
}
