package com.example.wallpaperapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun Navigation(getImageCategory: List<ImageItem>) {
    val myViewModel: MyViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CategoriesScreen.route) {

        composable(route = Screen.CategoriesScreen.route) {
            CategoriesScreen(
                navController = navController,
                getImageCategory = getImageCategory,
                myViewModel = myViewModel
            )
        }
        composable(route = Screen.ImagesScreen.route) { arg ->

            CategoryImagesScreen(
                navController = navController,
                myViewModel = myViewModel
            )

        }
        composable(route = Screen.SelectedImageScreen.route) { arg ->
            SelectedImage(image = myViewModel.imageLink.value)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    getImageCategory: List<ImageItem>,
    myViewModel: MyViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xfff3edf7),
                    titleContentColor = Color.Black
                ),
                title = {
                    Text(text = "Wallpaper categories")
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
                                myViewModel.category.value = model.categoryName
                                myViewModel.listImages.value = model.imageUrl
                                navController.navigate(route = Screen.ImagesScreen.route)
                            },
                        contentAlignment = Alignment.BottomStart
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(model.imageUrl[0])
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryImagesScreen(
    navController: NavController,
    myViewModel: MyViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xfff3edf7),
                    titleContentColor = Color.Black
                ),
                actions = {
                    IconButton(onClick = {/* go back */ },
                        modifier = Modifier.align(Alignment.CenterVertically)) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Application settings"
                        )
                    }
                },
                title = {
                    Text(text = myViewModel.category.value)
                }
            )
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = it,
            content = {
                items(myViewModel.listImages.value) { model ->
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                myViewModel.imageLink.value = model
                                navController.navigate(Screen.SelectedImageScreen.route)
                            },
                        contentAlignment = Alignment.BottomStart
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(model)
                                .build(),
                            contentDescription = myViewModel.category.value,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun SelectedImage(image: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.White
            )
        ) {
            Text(text = "Установить в качестве обоев")
        }

    }
}