package com.example.roomshopitems

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navHostController = rememberNavController()
    val categoryViewModel: CategorViewModel = viewModel()
    val itemViewModel: ItemViewModel = viewModel()

    NavHost(
        navController = navHostController,
        startDestination = ScreensRoute.ITEMS_SCREEN
    ) {
        composable(route = ScreensRoute.CATEGORIES_SCREEN) {

            CategoryScreen(categoryViewModel, navHostController)
        }
        composable(route = ScreensRoute.ITEMS_SCREEN) {
            ItemsScreen(itemViewModel, navHostController, categoryViewModel)
        }
    }
}