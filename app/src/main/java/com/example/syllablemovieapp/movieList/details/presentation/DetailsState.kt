package com.example.syllablemovieapp.movieList.details.presentation

import com.example.syllablemovieapp.movieList.domain.model.Movie

data class DetailsState(
    val isLoading : Boolean = false,
    val movie : Movie? = null
)
