package com.codepep.yps

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.model.RedditHotViewModel
import com.codepep.yps.model.ViewModelState
import com.codepep.yps.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ListCreator(viewModel : RedditHotViewModel, pullRefreshState: PullRefreshState) {
        val state by viewModel.state.collectAsState()
        when (state) {
            is ViewModelState.FAILURE -> {
                Text(text = "An error occurred")
            }
            is ViewModelState.LOADING -> {
                CircularProgressIndicator(modifier = Modifier.size(25.dp),
                    color = Color.Blue,
                    strokeWidth = 10.dp)
            }
            is ViewModelState.SUCCESS -> {
                val items = (state as ViewModelState.SUCCESS).items
                HotTopicsListEnhanced(viewModel, items, pullRefreshState)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainView() {
        val viewModel : RedditHotViewModel = viewModel()
        val state by viewModel.state.collectAsState()
        val refreshing = state == ViewModelState.LOADING
        val pullRefreshState
        = rememberPullRefreshState(refreshing && viewModel.hasLoaded(),
            { viewModel.refresh() })
        ListCreator(viewModel, pullRefreshState)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun HotTopicsListEnhanced(viewModel : RedditHotViewModel,
                              items: MutableList<RedditSubBottomLevelData>,
                                pullRefreshState: PullRefreshState) {
        val context = LocalContext.current
        val listState = rememberLazyListState()
        LazyColumn(Modifier.pullRefresh(pullRefreshState), state = listState) {
            itemsIndexed(items) { _, item ->
                RedditCardItem(item, context)
            }
        }

        val isScrollToEnd by remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
            }
        }
        val state by viewModel.state.collectAsState()
        LaunchedEffect(isScrollToEnd) {
            if (state != ViewModelState.LOADING) {
                viewModel.loadHotTopics()
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RedditCardItem(item: RedditSubBottomLevelData, context: Context) {
        Card(
            onClick = {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.data.url))
                startActivity(context, webIntent, null)
            },
            modifier = Modifier.padding(8.dp),
            elevation = 6.dp,
            backgroundColor = Color.White) {
            Row(modifier = Modifier
                .padding(4.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(item.data.thumbnail),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = item.data.title?:"",
                        modifier = Modifier.padding(4.dp),
                        color = Color.Black,
                        20.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = item.data.author?:"",
                        modifier = Modifier.padding(4.dp),
                        color = Color.Black,
                        12.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}