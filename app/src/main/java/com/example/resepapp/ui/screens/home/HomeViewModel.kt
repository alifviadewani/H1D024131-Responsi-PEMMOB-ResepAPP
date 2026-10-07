package com.example.resepapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepapp.data.model.Meal
import com.example.resepapp.data.remote.RetrofitClient
import com.example.resepapp.data.repository.MealRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.IOException

/** Semua kemungkinan kondisi layar Home. UI hanya "menggambar" berdasarkan state ini. */
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val meals: List<Meal>) : HomeUiState
    data object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel : ViewModel() {

    private val repository = MealRepository(RetrofitClient.api)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Setiap kali nilai berubah, pemuatan data diulang (dipakai oleh tombol "Coba Lagi").
    private val retryTick = MutableStateFlow(0)

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            combine(
                // Debounce 500 ms agar API tidak dipanggil di setiap ketikan; query kosong langsung diproses.
                _query.debounce { if (it.isBlank()) 0L else 500L }.distinctUntilChanged(),
                retryTick
            ) { q, _ -> q }
                .collectLatest { q -> loadMeals(q) } // collectLatest: request lama dibatalkan jika ada query baru
        }
    }

    init {
        observeQuery()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    fun retry() {
        retryTick.value += 1
    }

    private suspend fun loadMeals(query: String) {
        _uiState.value = HomeUiState.Loading
        _selectedCategory.value = null

        val result = if (query.isBlank()) {
            repository.getDefaultMeals()
        } else {
            repository.searchMeals(query.trim())
        }

        result.fold(
            onSuccess = { meals ->
                _uiState.value = if (meals.isEmpty()) HomeUiState.Empty else HomeUiState.Success(meals)
            },
            onFailure = { e ->
                _uiState.value = HomeUiState.Error(e.toUserMessage())
            }
        )
    }
}

/** Extension function: pesan error yang ramah pengguna. */
fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "Gagal terhubung ke server. Periksa koneksi internet kamu."
    else -> message ?: "Terjadi kesalahan yang tidak diketahui."
}
