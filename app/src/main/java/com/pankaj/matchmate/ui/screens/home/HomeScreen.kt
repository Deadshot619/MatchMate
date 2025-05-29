package com.pankaj.matchmate.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pankaj.matchmate.R

@Composable
fun HomeScreen(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.size(14.dp),
            text = stringResource(R.string.title_home_screen)
        )
    }
}