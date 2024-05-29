package com.example.syllablemovieapp.movieList.data.remote

import com.example.syllablemovieapp.movieList.data.remote.response.MovieListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMoviesList (
        @Path("category") category: String,
        @Query("page") page : Int,
        @Query("api_key") apiKey : String = API_KEY
    ) : MovieListDTO

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "06e82dfaa48a0b073767e075b3992bfe"
    }
}