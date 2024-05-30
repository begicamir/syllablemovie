package com.example.syllablemovieapp.movieList.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.syllablemovieapp.movieList.presentation.MovieListEvent
import com.example.syllablemovieapp.movieList.presentation.MovieListViewModel
import com.example.syllablemovieapp.movieList.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController
) {


    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
         BottomNavigationBar(bottomNavController = bottomNavController, onEvent = movieListViewModel::onEvent) 

        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (movieState.isCurrentPopularScreen) "Popular Movies"
                        else "Upcoming Movies",
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            NavHost(navController = bottomNavController, startDestination = Screen.PopularMovieList.route ) {
               composable(Screen.PopularMovieList.route){
                  PopularMovieScreen(navHostController = navController, movieState = movieState, onEvent = movieListViewModel::onEvent )
               }
               composable(Screen.UpcomingMovieList.route){
                  UpcomingMovieScreen(navHostController = navController, movieState = movieState, onEvent = movieListViewModel::onEvent )
               }
            }
        }
    }

}

@Composable
fun BottomNavigationBar(bottomNavController : NavHostController, onEvent : (MovieListEvent) -> Unit){

    val items = listOf(
        BottomItem(
        title = "Popular",
        icon = Icons.Rounded.Movie
        ),
        BottomItem(
           title = "Upcoming",
           icon = Icons.Rounded.Upcoming)
        )



    val selected = remember{
        mutableStateOf(BottomItem1.Popular)
    }

    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)){
            BottomItem1.entries.forEachIndexed {
                index, bottomItem ->
                NavigationBarItem(
                    selected = selected.value == bottomItem,
                    onClick = {
                             selected.value = bottomItem

                            when(selected.value){


                                BottomItem1.Popular -> {
                                    onEvent(MovieListEvent.Navigate(bottomItem))
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.PopularMovieList.route)
                                }
                                BottomItem1.Upcoming -> {
                                    onEvent(MovieListEvent.Navigate(bottomItem))
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(Screen.UpcomingMovieList.route)

                                }
                            }
                    },
                    icon = { Icon(imageVector = bottomItem.icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground) },
                    label = {
                        Text(text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground)
                    })
            }
        }
    }

}


data class BottomItem(
    val title : String,
    val icon : ImageVector
)
enum class BottomItem1( val title : String,
                        val icon : ImageVector){
    Popular (title = "Popular",
        icon = Icons.Rounded.Movie),

    Upcoming ( title = "Upcoming",
    icon = Icons.Rounded.Upcoming)
}