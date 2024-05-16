package com.erenalparslan.movieapp.details.presentation

import com.erenalparslan.movieapp.movieList.domain.model.Movie


data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
