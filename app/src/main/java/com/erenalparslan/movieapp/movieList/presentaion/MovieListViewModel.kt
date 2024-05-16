package com.erenalparslan.movieapp.movieList.presentaion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erenalparslan.movieapp.movieList.domain.repository.MovieListRepository
import com.erenalparslan.movieapp.movieList.util.Category
import com.erenalparslan.movieapp.movieList.util.Resource
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

    // Create a MutableStateFlow to hold the state of the movie list
    private var _movieListState = MutableStateFlow(MovieListState())

    // Expose the state as a read-only StateFlow
    val movieListState = _movieListState.asStateFlow()

    // Initialize the ViewModel by loading the popular and upcoming movies
    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    // Handle different UI events
    fun onEvent(event: MovieListUiEvent) {
        when (event) {
            // Navigate event toggles the current screen between popular and upcoming movies
            MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }
            }
            // Paginate event fetches more movies for the given category
            is MovieListUiEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
                }
            }
        }
    }

    // Fetch popular movies from the repository
    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            // Update state to indicate loading
            _movieListState.update {
                it.copy(isLoading = true)
            }

            // Fetch popular movies from the repository
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        // On error, update state to stop loading
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        // On success, update the movie list and increment the page number
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                            + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        // Update loading state
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    // Fetch upcoming movies from the repository
    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            // Update state to indicate loading
            _movieListState.update {
                it.copy(isLoading = true)
            }

            // Fetch upcoming movies from the repository
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        // On error, update state to stop loading
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        // On success, update the movie list and increment the page number
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList
                                            + upcomingList.shuffled(),
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        // Update loading state
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

}
