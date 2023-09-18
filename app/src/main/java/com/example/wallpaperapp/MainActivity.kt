package com.example.wallpaperapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wallpaperapp.retrofit.DataResponseModel
import com.example.wallpaperapp.ui.theme.CustomJCTheme
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
                CustomJCTheme {
                    Navigation(getImageCategory =  it)
                }
            }
        }
    }

    private fun getImageCategory(
        categories: List<String>,
        api: ImagesAPI,
        callback: (MutableList<ImageItem>) -> Unit
    ) {
        val listImageItem: MutableList<ImageItem> = mutableListOf()

        for (i in categories.indices) {
            api.getResponse(API_KEY, categories[i], "100", "relevance")
                .enqueue(object : Callback<DataResponseModel> {
                    override fun onResponse(
                        call: Call<DataResponseModel>,
                        response: Response<DataResponseModel>
                    ) {
                        if (response.body() == null || response.body()!!.data.isEmpty()) {
                            Log.d(TAG, "Empty results!")
                        } else {
                            listImageItem.add(ImageItem(response.body()!!.data.map { it.path }, categories[i]))
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
