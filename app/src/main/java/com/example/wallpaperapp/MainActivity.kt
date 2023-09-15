package com.example.wallpaperapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.wallpaperapp.retrofit.DataResponseModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "my_tag"
const val API_KEY = "Xsa2P1K0bpU5iYIta3q5oVLtAA1sbUN6"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categories = listOf(
            "Fashion",
            "Nature",
            "Education",
            "Love",
            "People",
            "Religion",
            "Places",
            "Animals",
            "Computer",
            "Food",
            "Sport",
            "Travel",
            "Buildings",
            "Business",
            "Transport",
            "Music",
            "Sky",
            "Autumn",
            "Winter",
            "Summer",
            "Blue",
            "White",
            "Red",
            "Flowers",
            "Grass",
            "Drinks",
            "Green",
            "Mountain",
            "Moon",
            "Sun",
            "Cat",
            "Dog",
            "Darkness",
            "Art",
            "Technologies",
            "Street",
        )
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val api = Retrofit.Builder()
            .baseUrl("https://wallhaven.cc/api/v1/").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImagesAPI::class.java)

        getImageCategory(categories, api) {
            setContent {
                LazyVerticalGrid_(it)
            }
        }
    }

    private fun getImageCategory(
        categories: List<String>,
        api: ImagesAPI,
        callback: (List<ImageItem>) -> Unit
    ) {
        val listImageItem: MutableList<ImageItem> = mutableListOf()
        for (i in categories.indices) {
            api.getResponse(API_KEY, categories[i], 110, "relevance")
                .enqueue(object : Callback<DataResponseModel> {
                    override fun onResponse(
                        call: Call<DataResponseModel>,
                        response: Response<DataResponseModel>
                    ) {
                        if (response.body() == null || response.body()!!.data.isEmpty()) {
                            Log.d(TAG, "Empty results!")
                        } else {
                            listImageItem.add(
                                ImageItem(
                                    response.body()!!.data[0].path,
                                    categories[i]
                                )
                            )
                            callback(listImageItem)
                        }
                    }

                    override fun onFailure(call: Call<DataResponseModel>, t: Throwable) {
                        Log.d(TAG, "OnFailure")
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyVerticalGrid_(getImageCategory: List<ImageItem>) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xfff3edf7),
                    titleContentColor = Color.Black
                ),
                title = {
                    Text(
                        text = "Wallpaper categories",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {/* do smth */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Application settings"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = innerPadding,
            content = {
                items(getImageCategory) { model ->
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clickable {
                                Log.d(TAG, "pressed on ${model.categoryName}")
                            },
                        contentAlignment = Alignment.BottomStart
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(model.imageUrl)
                                .build(),
                            contentDescription = "Category",
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawWithCache {
                                    val gradient = Brush.linearGradient(
                                        colors = listOf(
                                            Color.White,
                                            Color.Transparent
                                        )
                                    )
                                    onDrawBehind {
                                        drawRect(gradient)
                                    }
                                }
                                .padding(start = 7.dp),
                            text = model.categoryName,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        )
    }
}
