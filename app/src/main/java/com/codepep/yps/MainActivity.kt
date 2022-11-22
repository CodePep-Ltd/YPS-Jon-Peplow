package com.codepep.yps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codepep.yps.model.RedditHotViewModel
import com.codepep.yps.model.ViewModelState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }
}

@Composable
fun MainView() {
    val viewModel : RedditHotViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    when (state) {
        is ViewModelState.START-> {
        }
        is ViewModelState.FAILURE -> {
        }
        is ViewModelState.LOADING -> {
        }
        is ViewModelState.SUCCESS -> {
            val item = (state as ViewModelState.SUCCESS).item
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}