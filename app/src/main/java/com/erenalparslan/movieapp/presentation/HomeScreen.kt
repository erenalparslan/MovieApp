package com.erenalparslan.movieapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erenalparslan.movieapp.R
import com.erenalparslan.movieapp.movieList.presentaion.MovieListUiEvent
import com.erenalparslan.movieapp.movieList.presentaion.MovieListViewModel
import com.erenalparslan.movieapp.movieList.presentaion.PopularMoviesScreen
import com.erenalparslan.movieapp.movieList.presentaion.UpcomingMoviesScreen
import com.erenalparslan.movieapp.movieList.util.Screen

// This screen welcomes us first when we open the app.

// The ExperimentalMaterial3Api annotation allows us to use experimental APIs from the Material3 library.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    // Retrieve the MovieListViewModel using Hilt.
    val movieListViewModel = hiltViewModel<MovieListViewModel>()

    // Collect the movie list state from the ViewModel.
    val movieListState = movieListViewModel.movieListState.collectAsState().value

    // Create a separate NavController for the bottom navigation bar.
    val bottomNavController = rememberNavController()

    // Scaffold provides a standard page layout in Material Design.
    Scaffold(
        bottomBar = {
            // Add the bottom navigation bar.
            BottomNavigationBar(
                bottomNavController = bottomNavController, onEvent = movieListViewModel::onEvent
            )
        },
        topBar = {
            // Add the top navigation bar.
            TopAppBar(
                title = {
                    // The title of the top bar changes based on the current screen.
                    Text(
                        text = if (movieListState.isCurrentPopularScreen)
                            stringResource(R.string.popular_movies)
                        else
                            stringResource(R.string.upcoming_movies),
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        }
    ) {
        // Scaffold content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // NavHost provides in-app navigation.
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.rout
            ) {
                // Popular movies screen
                composable(Screen.PopularMovieList.rout) {
                    PopularMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                // Upcoming movies screen
                composable(Screen.UpcomingMovieList.rout) {
                    UpcomingMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }
}

// Creates the bottom navigation bar.
@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController, onEvent: (MovieListUiEvent) -> Unit
) {

    // Navigation bar items
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = stringResource(R.string.upcoming),
            icon = Icons.Rounded.Upcoming
        )
    )

    // Create a state to remember which item is selected.
    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    // NavigationBar creates the bottom navigation bar.
    NavigationBar {
        // Row layout for navigation items
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomItem ->
                // Each navigation bar item
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        // Actions to take when an item is clicked
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.PopularMovieList.rout)
                            }

                            1 -> {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.UpcomingMovieList.rout)
                            }
                        }
                    },
                    icon = {
                        // Icon for the item
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        // Label for the item
                        Text(
                            text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}

// Data class for bottom navigation bar items
data class BottomItem(
    val title: String, val icon: ImageVector
)
