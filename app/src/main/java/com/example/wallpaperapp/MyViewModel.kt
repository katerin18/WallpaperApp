package com.example.wallpaperapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    var imageLink: MutableState<String> = mutableStateOf("")
    var listImages: MutableState<List<String>> = mutableStateOf(listOf())
    var category: MutableState<String> = mutableStateOf("")
}