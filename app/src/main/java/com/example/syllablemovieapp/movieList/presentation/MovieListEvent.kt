package com.example.syllablemovieapp.movieList.presentation

sealed interface MovieListEvent {

    data class Paginate(val category: String) : MovieListEvent
    data object Navigate : MovieListEvent
}