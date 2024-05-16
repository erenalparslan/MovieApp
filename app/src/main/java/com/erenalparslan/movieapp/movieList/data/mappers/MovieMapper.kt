package com.erenalparslan.movieapp.movieList.data.mappers

import com.erenalparslan.movieapp.movieList.data.local.movie.MovieEntity
import com.erenalparslan.movieapp.movieList.data.remote.respnod.MovieDto
import com.erenalparslan.movieapp.movieList.domain.model.Movie


fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        // Map each field from MovieDto to MovieEntity, providing default values if necessary
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: -1,
        original_title = original_title ?: "",
        video = video ?: false,

        // Add the category which is provided as a parameter
        category = category,

        // Convert the list of genre IDs to a comma-separated string, handling potential exceptions
        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

// Extension function to convert MovieEntity to Movie
fun MovieEntity.toMovie(
    category: String
): Movie {
    return Movie(
        // Map each field from MovieEntity to Movie
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        video = video,
        id = id,
        adult = adult,
        original_title = original_title,

        // Add the category which is provided as a parameter
        category = category,

        // Convert the comma-separated string of genre IDs back to a list of integers, handling potential exceptions
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        }
    )
}
