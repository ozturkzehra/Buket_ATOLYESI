package com.example.canimuygulamam // Burayı bu şekilde düzeltin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {//telefon dondugunda verilerimin silinmemesini sağlar
    private val auth = FirebaseAuth.getInstance()//db için gerekli yetkilendirme nesnemi baslattım

    // Ekranın durumunu /state tutan değişkenlerim
    //mvvn en onemli kuralı !encapsulatıon! ilkesine uygun bu kural
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onEmailChange(email: String) { _email.value = email }
    fun onPasswordChange(pass: String) { _password.value = pass }

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            auth.signInWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) onSuccess()
                    else onError(task.exception?.message ?: "Hata oluştu")
                }
        }
    }

}

