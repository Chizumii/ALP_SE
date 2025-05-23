//package com.example.alp_vp_jozz.viewModels
//
//// viewmodel/UserViewModel.kt
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.alp_vp_jozz.models.LoginUserRequest
//import com.example.alp_vp_jozz.models.RegisterUserRequest
//import com.example.alp_vp_jozz.models.UpdateUserRequest
//import com.example.alp_vp_jozz.models.UserResponse
//import com.example.alp_vp_jozz.repositories.UserRepository
//import kotlinx.coroutines.launch
//
//class UserViewModel(private val repository: UserRepository) : ViewModel() {
//
//    private val _userResponse = MutableLiveData<UserResponse>()
//    val userResponse: LiveData<UserResponse> = _userResponse
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    fun registerUser(request: RegisterUserRequest) {
//        viewModelScope.launch {
//            try {
////                val response = repository.registerUser(request)
////                _userResponse.value = response
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//            }
//        }
//    }
//
//    fun loginUser(request: LoginUserRequest) {
//        viewModelScope.launch {
//            try {
////                val response = repository.loginUser(request)
////                _userResponse.value = response
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//            }
//        }
//    }
//
//    fun updateUser(request: UpdateUserRequest) {
//        viewModelScope.launch {
//            try {
////                val response = repository.updateUser(request)
////                _userResponse.value = response
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//            }
//        }
//    }
//
//    fun logoutUser() {
//        viewModelScope.launch {
//            try {
////                repository.logoutUser()
//                _userResponse.value = null
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//            }
//        }
//    }
//}