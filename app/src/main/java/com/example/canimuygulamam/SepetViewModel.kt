package com.example.canimuygulamam

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartItem(
    val flowers: List<Flower>,
    val cardboardColor: String,
    val tulleColor: String,
    val ribbonColor: String,
    val cardColor: String,
    val cardText: String,
    val totalPrice: Int
)

class SepetViewModel : ViewModel() {
    private val _sepetList = MutableStateFlow<List<CartItem>>(emptyList())
    val sepetList = _sepetList.asStateFlow()

    fun sepeteEkle(item: CartItem) {
        _sepetList.value = _sepetList.value + item
    }

    fun sepettenCikar(item: CartItem) {
        _sepetList.value = _sepetList.value - item
    }
}
