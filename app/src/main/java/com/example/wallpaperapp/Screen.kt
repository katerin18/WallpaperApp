package com.example.wallpaperapp

sealed class Screen(val route: String) {
    object CategoriesScreen: Screen("category_screen")
    object ImagesScreen: Screen("images_screen")
    object SelectedImageScreen: Screen("selected_image_screen")
}