package com.erenalparslan.movieapp.movieList.presentaion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.erenalparslan.movieapp.movieList.presentaion.componentes.MovieItem
import com.erenalparslan.movieapp.movieList.util.Category


@Composable
fun PopularMoviesScreen(
    movieListState: MovieListState, // The state of the popular movies list
    navController: NavHostController, // Navigation controller for navigating to movie details
    onEvent: (MovieListUiEvent) -> Unit // Event listener for handling UI events
) {

    // If the popular movie list is empty, show a loading indicator
    if (movieListState.popularMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Show a circular progress indicator while loading
        }
    } else {
        // If the popular movie list is not empty, display the list
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Display in a grid with 2 columns
            modifier = Modifier.fillMaxSize(), // Take up the maximum available space
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 4.dp
            ) // Add padding around the content
        ) {
            items(movieListState.popularMovieList.size) { index ->
                // Display each movie item in the list
                MovieItem(
                    movie = movieListState.popularMovieList[index], // Pass the movie item
                    navHostController = navController // Pass the navigation controller
                )
                Spacer(modifier = Modifier.height(16.dp)) // Add spacing between movie items

                // If the last visible item in the list is reached and not loading more data, trigger pagination
                if (index >= movieListState.popularMovieList.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.POPULAR)) // Trigger pagination event
                }
            }
        }
    }
}


