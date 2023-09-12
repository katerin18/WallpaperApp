package com.example.wallpaperapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

const val TAG = "my_tag"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmallTopAppBar()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SmallTopAppBar() {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xfff3edf7),
                        titleContentColor = Color.Black
                    ),
                    title = {
                        Text(text = "Wallpaper categories",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    },
                    actions = {
                        IconButton(onClick = {/* do smth */}) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Application settings"
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
           Log.d(TAG, "innerPadding = $innerPadding")
        }
    }
}
