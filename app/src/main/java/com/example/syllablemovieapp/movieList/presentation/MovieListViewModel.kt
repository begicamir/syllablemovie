package com.example.syllablemovieapp.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.syllablemovieapp.movieList.core.presentation.BottomItem1
import com.example.syllablemovieapp.movieList.domain.repository.MovieListRepository
import com.example.syllablemovieapp.movieList.util.Category
import com.example.syllablemovieapp.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }
    fun onEvent(event : MovieListEvent){
        when(event){

            is MovieListEvent.Navigate -> {
                _movieListState.update {
                    it.copy(isCurrentPopularScreen = event.bottomItem == BottomItem1.Popular)
                }
            }
            is MovieListEvent.Paginate -> {
                if (event.category == Category.POPULAR){
                    getPopularMovieList(true)
                }
                else {
                    getUpcomingMovieList(true)
                }
            }
        }
    }
    private fun getPopularMovieList(forceFetchFromRemote : Boolean){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(forceFetchFromRemote, Category.POPULAR, movieListState.value.PopularMoviePage).collectLatest {
                result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let{ popularMovieList ->
                            _movieListState.update {
                                it.copy(popularMovieList = movieListState.value.popularMovieList+popularMovieList.shuffled(), PopularMoviePage = movieListState.value.PopularMoviePage + 1)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote : Boolean){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(forceFetchFromRemote, Category.UPCOMING, movieListState.value.UpcomingMoviePage).collectLatest {
                    result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let{ upcomingMovieList ->
                            _movieListState.update {
                                it.copy(upcomingMovieList = movieListState.value.upcomingMovieList+upcomingMovieList.shuffled(), UpcomingMoviePage = movieListState.value.UpcomingMoviePage + 1)
                            }
                        }
                    }
                }
            }
        }
    }
}