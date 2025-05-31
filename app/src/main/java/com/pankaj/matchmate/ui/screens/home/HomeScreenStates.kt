package com.pankaj.matchmate.ui.screens.home

import androidx.compose.runtime.Immutable
import com.pankaj.matchmate.domain.domainmodels.UserDomain
import com.pankaj.matchmate.repository.db.MatchStatus

@Immutable
data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val matches: List<UserDomain> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String = ""
)

sealed class HomeScreenEffects {
    data class showSnackbar(val message: String) : HomeScreenEffects()
}

