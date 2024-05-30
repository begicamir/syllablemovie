package com.example.syllablemovieapp.movieList.presentation

import com.example.syllablemovieapp.movieList.core.presentation.BottomItem
import com.example.syllablemovieapp.movieList.core.presentation.BottomItem1

sealed interface MovieListEvent {

    data class Paginate(val category: String) : MovieListEvent
    data class Navigate(val bottomItem: BottomItem1) : MovieListEvent
}