package com.example.canimuygulamam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onEmailChange(email: String) { _email.value = email }
    fun onPasswordChange(pass: String) { _password.value = pass }
    fun onConfirmPasswordChange(pass: String) { _confirmPassword.value = pass }

    fun signup(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_password.value != _confirmPassword.value) {
            onError("Şifreler eşleşmiyor")
            return
        }
        
        if (_password.value.length < 6) {
            onError("Şifre en az 6 karakter olmalıdır")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(task.exception?.message ?: "Kayıt hatası")
                    }
                }
        }
    }
}
