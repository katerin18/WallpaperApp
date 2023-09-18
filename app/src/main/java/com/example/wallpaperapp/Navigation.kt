package com.example.wallpaperapp

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.wallpaperapp.ui.theme.CustomTheme
import com.example.wallpaperapp.ui.theme.CustomThemeManager

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
        composable(route = Screen.ImagesScreen.route) {
            CategoryImagesScreen(
                navController = navController,
                myViewModel = myViewModel
            )
        }
        composable(route = Screen.SelectedImageScreen.route) {
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
    var expandedSettings by remember { mutableStateOf(false) }
    var switchTheme by remember { mutableStateOf(false) }
    var switchImages by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = CustomThemeManager.colors.backgroundColor,
                ),
                title = {
                    Text(text = "Wallpaper categories", color = CustomThemeManager.colors.textColor)
                },

                actions = {
                    IconButton(onClick = { expandedSettings = !expandedSettings }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Application settings",
                            tint = CustomThemeManager.colors.iconColor
                        )
                    }
                    DropdownMenu(
                        expanded = expandedSettings,
                        onDismissRequest = { expandedSettings = false }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 7.dp, start = 7.dp, end = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Switch app theme",
                                color = CustomThemeManager.colors.textColor
                            )
                            Switch(
                                checked = switchTheme,
                                onCheckedChange = { switchTheme = !switchTheme },
                                thumbContent = {
                                    when (switchTheme) {
                                        true -> CustomThemeManager.customTheme = CustomTheme.DARK
                                        false -> CustomThemeManager.customTheme = CustomTheme.LIGHT
                                    }
                                }
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 7.dp, start = 7.dp, end = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Allow show 18+ images",
                                modifier = Modifier.padding(end = 5.dp),
                                color = CustomThemeManager.colors.textColor
                            )
                            Switch(
                                checked = switchImages,
                                onCheckedChange = { switchImages = !switchImages },
                                thumbContent = {
                                    Toast.makeText(
                                        LocalContext.current,
                                        "Search images was changed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
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
                        Box(modifier = Modifier.background(CustomThemeManager.colors.buttonBackgroundColor)) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 7.dp),
                                text = model.categoryName,
                                fontSize = 18.sp,
                                color = CustomThemeManager.colors.textColor
                            )
                        }
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
                title = {
                    Text(
                        text = myViewModel.category.value,
                        color = CustomThemeManager.colors.textColor
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = CustomThemeManager.colors.backgroundColor
                ),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Application settings",
                                tint = CustomThemeManager.colors.iconColor
                            )
                        }
                    }
                },
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
    val context = LocalContext.current
    val hasPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.SET_WALLPAPER
        ) == PackageManager.PERMISSION_GRANTED
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(bottom = 5.dp),
                onClick = {
                    setWallpaperLock(context, hasPermission, image, "lock")
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = CustomThemeManager.colors.textColor,
                    containerColor = CustomThemeManager.colors.buttonBackgroundColor
                )
            ) {
                Text(text = "Set as lock screen")
            }

            Button(
                modifier = Modifier
                    .padding(bottom = 5.dp),
                onClick = {
                    setWallpaperLock(context, hasPermission, image, "system")
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = CustomThemeManager.colors.textColor,
                    containerColor = CustomThemeManager.colors.buttonBackgroundColor
                )
            ) {
                Text(text = "Set as system screen")
            }

            Button(
                modifier = Modifier
                    .padding(bottom = 5.dp),
                onClick = {
                    setWallpaperLock(context, hasPermission, image, "lock&system")
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = CustomThemeManager.colors.textColor,
                    containerColor = CustomThemeManager.colors.buttonBackgroundColor
                )
            ) {
                Text(text = "Set everywhere")
            }
        }

    }
}

fun setWallpaperLock(context: Context, hasPermission: Boolean, image: String, flag: String) {
    if (hasPermission) {
        Glide.with(context)
            .asBitmap()
            .load(image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(context, "Setting wallpaper error", Toast.LENGTH_SHORT).show()
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val manager = WallpaperManager.getInstance(context)
                    when (flag) {
                        "lock" -> manager.setBitmap(
                            resource,
                            null,
                            true,
                            WallpaperManager.FLAG_LOCK
                        )

                        "system" -> manager.setBitmap(
                            resource,
                            null,
                            true,
                            WallpaperManager.FLAG_SYSTEM
                        )

                        "lock&system" -> manager.setBitmap(resource)
                    }
                    Toast.makeText(
                        context,
                        "Wallpaper has installed successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.SET_WALLPAPER),
            120
        )
    }
}