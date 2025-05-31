package com.pankaj.matchmate.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pankaj.matchmate.R
import com.pankaj.matchmate.domain.domainmodels.UserDomain
import com.pankaj.matchmate.repository.db.MatchStatus

@Composable
fun HomeScreen(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    LaunchedEffect(isConnected) {
        if(isConnected) {
            if(uiState.isLoading.not() && uiState.matches.isEmpty())
                viewModel.getMatches()
            snackbarHostState.showSnackbar("Internet Connected")
        } else {
            snackbarHostState.showSnackbar("No Internet Connection, Please connect to internet.")
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        when  {
            uiState.isError && uiState.errorMessage.isNotEmpty() && uiState.matches.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                        text = uiState.errorMessage
                    )
                }
            }
            uiState.isLoading && uiState.matches.isEmpty() && uiState.isError.not() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.matches.isNotEmpty() -> {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = TextUnit(24f, TextUnitType.Sp),
                    text = stringResource(R.string.title_home_screen)
                )
                LazyColumn {
                    items(uiState.matches, key = { it.id }) { user ->
                        MatchCard(
                            modifier = Modifier,
                            userDomain = user,
                            status = user.matchStatus,
                            onAccept = {
                                viewModel.updateMatch(user.id, MatchStatus.ACCEPTED)
                            },
                            onDecline = {
                                viewModel.updateMatch(user.id, MatchStatus.DECLINED)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCard(
    modifier: Modifier,
    userDomain: UserDomain,
    status: MatchStatus,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = modifier.padding(12.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(userDomain.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(256.dp).clip(RoundedCornerShape(16.dp)),
                alignment = Alignment.Center,
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = userDomain.name,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Age: ${userDomain.age} • Location: ${userDomain.location}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (status) {
                MatchStatus.ACCEPTED -> StatusBanner(stringResource(R.string.status_accepted), Color(0xFF4CAF50))
                MatchStatus.DECLINED -> StatusBanner(stringResource(R.string.status_declined), Color(0xFFF44336))
                else -> Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDecline, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.btn_decline))
                    }
                    Button(onClick = onAccept, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.btn_accept))
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBanner(text: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(text, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}
