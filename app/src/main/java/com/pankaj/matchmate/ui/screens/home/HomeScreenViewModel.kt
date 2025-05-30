package com.pankaj.matchmate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankaj.matchmate.domain.domainmodels.toUserDomain
import com.pankaj.matchmate.repository.MatchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pankaj.matchmate.repository.Result
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val matchesRepository: MatchesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getMatches()
    }

    private fun getMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            matchesRepository.getMatches().flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.value = HomeScreenUiState.Loading
                    is Result.Success -> _uiState.value = HomeScreenUiState.Success(
                        result.data?.map { it.toUserDomain() } ?: emptyList()
                    )
                    is Result.Error -> _uiState.value = HomeScreenUiState.Error(result.message ?: "")
                }
            }
        }
    }
}