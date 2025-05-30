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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pankaj.matchmate.R
import com.pankaj.matchmate.domain.domainmodels.UserDomain
import com.pankaj.matchmate.repository.db.MatchStatus

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        when (val state = uiState.value) {
            is HomeScreenUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeScreenUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                        text = state.message
                    )
                }
            }
            is HomeScreenUiState.Success -> {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = TextUnit(24f, TextUnitType.Sp),
                    text = stringResource(R.string.title_home_screen)
                )
                LazyColumn {
                    items(state.matches, key = { it.id }) { user ->
                        MatchCard(
                            modifier = Modifier,
                            userDomain = user,
                            status = user.matchStatus,
                            onAccept = {

                            },
                            onDecline = {

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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(userDomain.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(72.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(userDomain.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Age: ${userDomain.age} • Location: ${userDomain.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            when (status) {
                MatchStatus.ACCEPTED -> StatusBanner("Member Accepted", Color(0xFF4CAF50))
                MatchStatus.DECLINED -> StatusBanner("Member Declined", Color(0xFFF44336))
                else -> Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDecline, modifier = Modifier.weight(1f)) {
                        Text("Decline")
                    }
                    Button(onClick = onAccept, modifier = Modifier.weight(1f)) {
                        Text("Accept")
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
