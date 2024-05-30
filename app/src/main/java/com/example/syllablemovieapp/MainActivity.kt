package com.example.syllablemovieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.syllablemovieapp.movieList.core.presentation.HomeScreen
import com.example.syllablemovieapp.movieList.util.Screen
import com.example.syllablemovieapp.ui.theme.SyllableMovieAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SyllableMovieAppTheme {
                setBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   val navController = rememberNavController()

                    NavHost(navController =navController , startDestination =  Screen.Home.route) {
                        composable(Screen.Home.route){
                            HomeScreen(navController)
                        }

                        composable(Screen.Details.route + "/{movieId}",
                            arguments = listOf(navArgument("movieId"){
                                type = NavType.IntType
                            })
                        ){ navBackStackEntry ->  
                     //       DetailsScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun setBarColor (color: androidx.compose.ui.graphics.Color){
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}
