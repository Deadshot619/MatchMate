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
import com.pankaj.matchmate.repository.db.MatchStatus
import com.pankaj.matchmate.utils.ConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val matchesRepository: MatchesRepository,
    val connectivityObserver: ConnectivityObserver
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    init {
        getMatches()
        collectMatchesList()
    }

    private fun collectMatchesList() {
        viewModelScope.launch {
            matchesRepository.matchesList.map {
                it.map { matchEntity -> matchEntity.toUserDomain() }
            }.collect { matches ->
                _uiState.update {
                    it.copy(matches = matches)
                }
            }
        }
    }

    fun getMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            matchesRepository.fetchAndSaveMatches(10).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update {
                        it.copy(isLoading = true, isError = false)
                    }
                    is Result.Success -> _uiState.update {
                        it.copy(isLoading = false, isError = false, errorMessage = "")
                    }
                    is Result.Error -> _uiState.update {
                        it.copy(isLoading = false, isError = true, errorMessage = result.message ?: "")
                    }
                }
            }
        }
    }

    fun updateMatch(id: String, matchStatus: MatchStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            matchesRepository.updateMatchStatus(id, matchStatus)
        }
    }
}