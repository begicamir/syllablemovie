package com.example.syllablemovieapp.movieList.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.syllablemovieapp.movieList.data.local.movie.MovieDB
import com.example.syllablemovieapp.movieList.data.mappers.toMovie
import com.example.syllablemovieapp.movieList.data.mappers.toMovieEntity
import com.example.syllablemovieapp.movieList.data.remote.MovieApi
import com.example.syllablemovieapp.movieList.domain.model.Movie
import com.example.syllablemovieapp.movieList.domain.repository.MovieListRepository
import com.example.syllablemovieapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDB: MovieDB
) : MovieListRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
       return flow {

           emit(Resource.Loading(true))

           val localMovieList = movieDB.movieDao.getMovieListByCategory(category)
           val shouldLoadFromDB = localMovieList.isNotEmpty() && !forceFetchFromRemote

           if (shouldLoadFromDB) {
               emit(Resource.Success(data = localMovieList.map {
                   it.toMovie(category)
               }))

               emit(Resource.Loading(false))
               return@flow
           }

           val movieListFromApi = try {
               movieApi.getMoviesList(category, page)

           }
           catch (e: IOException){
               e.printStackTrace()
               emit(Resource.Error(message = "Error Loading Movies"))
               return@flow
           }
           catch (e: HttpException){
               e.printStackTrace()
               emit(Resource.Error(message = "Error Loading Movies"))
               return@flow
           }
           catch (e: Exception){
               e.printStackTrace()
               emit(Resource.Error(message = "Error Loading Movies"))
               return@flow
           }

           val movieEntities = movieListFromApi.results.let{
               it.map{
                   movieDTO ->
                   movieDTO.toMovieEntity(category)
               }
           }

           movieDB.movieDao.upsertMovieList(movieEntities)
           emit(Resource.Success(
               movieEntities.map{
                   it.toMovie(category)
               }
           ))
           emit(Resource.Loading(false))
       }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDB.movieDao.getMovieById(id)

            if (movieEntity != null){
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("No such movie"))
            emit(Resource.Loading(false))
        }
    }
}