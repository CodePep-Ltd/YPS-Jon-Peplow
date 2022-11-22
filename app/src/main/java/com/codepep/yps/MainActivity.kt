package com.codepep.yps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codepep.yps.dto.RedditSubTopLevelData
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
}

@Composable
fun MainView() {
    val viewModel : RedditHotViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    when (state) {
        is ViewModelState.FAILURE -> {
            Text(text = "An error occurred")
        }
        is ViewModelState.LOADING -> {
            Text(text = "LOADING...")
        }
        is ViewModelState.SUCCESS -> {
            val item = (state as ViewModelState.SUCCESS).item
            HotTopicsList(item.data)
        }
    }
}

@Composable
fun HotTopicsList(hotTopic: RedditSubTopLevelData) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        itemsIndexed(hotTopic.children) { _, item ->
            Row(modifier = Modifier
                .padding(8.dp)
                .background(Color(255, 255, 255)),
                verticalAlignment = Alignment.CenterVertically) {
                }
                Column(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clickable {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.data.url))
                        startActivity(context, webIntent, null)
                    }
                ) {
                    Text(
                        text = item.data.title?:"",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black)
                    Text(
                        text = item.data.author?:"", fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
}