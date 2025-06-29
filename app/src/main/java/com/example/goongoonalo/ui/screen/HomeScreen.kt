package com.example.goongoonalo.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.pullrefresh.*
import androidx.compose.ui.Alignment
import com.example.goongoonalo.data.model.VideoItem
import com.example.goongoonalo.viewmodel.VideoViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: VideoViewModel = hiltViewModel()) {
    val newList by viewModel.newList.collectAsState()
    val topList by viewModel.topList.collectAsState()
    val continueList by viewModel.continueList.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.fetchVideos(clearPrefs = true) }
    )

    var selectedItem by remember { mutableStateOf<VideoItem?>(null) }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)
        .background(Color.Black)
    ) {

        if (isInitialLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                item { Section("New on MyTube", newList) { selectedItem = it } }
                item { Section("Top 10 in India today", topList) { selectedItem = it } }
                item { Section("Continue Watching", continueList) { selectedItem = it } }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    selectedItem?.let {
        DetailScreen(it) {
            viewModel.addToContinueWatching(it)
            selectedItem = null
        }
    }
}


@Composable
fun Section(title: String, list: List<VideoItem>, onClick: (VideoItem) -> Unit) {
    Column {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow {
            items(list) { item ->
                Image(
                    painter = rememberAsyncImagePainter(item.Thumbnail),
                    contentDescription = item.Title,
                    modifier = Modifier
                        .size(width = 140.dp, height = 180.dp)
                        .padding(end = 8.dp)
                        .clickable { onClick(item) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
