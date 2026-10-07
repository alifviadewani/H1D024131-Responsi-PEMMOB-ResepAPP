package com.example.resepapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepapp.data.model.Meal
import com.example.resepapp.data.remote.RetrofitClient
import com.example.resepapp.data.repository.MealRepository
import com.example.resepapp.ui.screens.home.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val meal: Meal) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = MealRepository(RetrofitClient.api)

    // "mealId" otomatis diisi dari argumen Navigation Compose
    private val mealId: String = savedStateHandle.get<String>("mealId").orEmpty()

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun retry() = loadDetail()

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            repository.getMealDetail(mealId).fold(
                onSuccess = { meal ->
                    _uiState.value = if (meal != null) {
                        DetailUiState.Success(meal)
                    } else {
                        DetailUiState.Error("Resep tidak ditemukan.")
                    }
                },
                onFailure = { e -> _uiState.value = DetailUiState.Error(e.toUserMessage()) }
            )
        }
    }
}
