package com.example.alp_se.uiStates

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.alp_se.R

data class AuthenticationUIState(
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val passwordVisibility: VisualTransformation = PasswordVisualTransformation(),
    val confirmPasswordVisibility: VisualTransformation = PasswordVisualTransformation(),
    val passwordVisibilityIcon: Int = R.drawable.baseline_visibility_24,
    val confirmPasswordVisibilityIcon: Int = R.drawable.baseline_visibility_24,
    val buttonEnabled: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)