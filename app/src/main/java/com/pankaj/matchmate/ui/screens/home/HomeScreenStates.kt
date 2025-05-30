package com.pankaj.matchmate.ui.screens.home

import com.pankaj.matchmate.domain.domainmodels.UserDomain
import com.pankaj.matchmate.repository.db.MatchStatus

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    data class Success(val matches: List<UserDomain>) : HomeScreenUiState()
    data class Error(val message: String) : HomeScreenUiState()
}

sealed class HomeScreenInteraction {
    data class UpdateMatchStatus(val id: String, val status: MatchStatus) : HomeScreenInteraction()
}