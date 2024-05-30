package com.example.syllablemovieapp.movieList.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.syllablemovieapp.movieList.core.presentation.components.MovieItem
import com.example.syllablemovieapp.movieList.presentation.MovieListEvent
import com.example.syllablemovieapp.movieList.presentation.MovieListState
import com.example.syllablemovieapp.movieList.util.Category

@Composable
fun PopularMovieScreen(
    movieState : MovieListState,
    navHostController: NavHostController,
    onEvent : (MovieListEvent) -> Unit
) = if(movieState.popularMovieList.isEmpty()){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        CircularProgressIndicator()
    }
}
else {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
    ) {
        items(movieState.popularMovieList.size){ index ->
            MovieItem(movie = movieState.popularMovieList[index], navHostController = navHostController)

            Spacer(modifier = Modifier.height(6.dp))

            if (index >= movieState.popularMovieList.lastIndex && !movieState.isLoading){
                onEvent(MovieListEvent.Paginate(Category.UPCOMING))
            }


        }

    }
}