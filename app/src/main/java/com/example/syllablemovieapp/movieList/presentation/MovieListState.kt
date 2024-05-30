package com.example.syllablemovieapp.movieList.presentation

import com.example.syllablemovieapp.movieList.domain.model.Movie

data class MovieListState (

    val isLoading : Boolean = false,
    val PopularMoviePage : Int = 1,
    val UpcomingMoviePage : Int = 1,
    val isCurrentPopularScreen : Boolean = true,
    val popularMovieList : List<Movie> = emptyList(),
    val upcomingMovieList : List<Movie> = emptyList()
)