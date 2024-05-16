package com.erenalparslan.movieapp.movieList.presentaion


sealed interface MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent
    object Navigate : MovieListUiEvent
}