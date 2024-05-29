package com.example.syllablemovieapp.movieList.data.remote.response

data class MovieListDTO(
    val dates: Dates,
    val page: Int,
    val results: List<MovieDTO>,
    val total_pages: Int,
    val total_results: Int
)