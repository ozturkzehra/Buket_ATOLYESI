package com.example.canimuygulamam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.canimuygulamam.ui.theme.AppTypography
import com.example.canimuygulamam.ui.profile.ProfileScreen
import com.example.canimuygulamam.ui.auth.LoginScreen
import com.example.canimuygulamam.ui.auth.SignupScreen
import com.example.canimuygulamam.ui.home.FlowerDetailScreen
import com.example.canimuygulamam.ui.home.MainScreen
import com.example.canimuygulamam.ui.sepet.SepetScreen
import com.example.canimuygulamam.ui.sepet.SepetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val customColorScheme = lightColorScheme(
                primary = colorResource(id = R.color.evergreen),
                surface = colorResource(id = R.color.mist),
                onSurface = colorResource(id = R.color.evergreen),
                secondary = colorResource(id = R.color.sage),
                onPrimary = colorResource(id = R.color.white)
            )

            MaterialTheme(
                colorScheme = customColorScheme,
                typography = AppTypography
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.son),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Ortak sepet yönetimi için tek bir ViewModel örneği (Instance)
    val sepetViewModel: SepetViewModel = viewModel()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("main") { MainScreen(navController, sepetViewModel) }
        composable("sepet") { SepetScreen(navController, sepetViewModel) }
        composable("profil") { ProfileScreen(navController) }

        // Yeni Çoklu Çiçek ve Tasarım Stüdyosu Rotası (Hatalı kısım düzeltildi 🛠️)
        composable("flowerDetail/studio") {
            FlowerDetailScreen(
                navController = navController,
                sepetViewModel = sepetViewModel
            )
        }
    }
}