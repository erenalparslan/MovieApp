package com.erenalparslan.movieapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.erenalparslan.movieapp.details.presentation.DetailsScreen
import com.erenalparslan.movieapp.movieList.util.Screen
import com.erenalparslan.movieapp.theme.MoviesAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                //For status bar change color
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Nav Host should been at MainActivity
                    val navController = rememberNavController()

                    //Routs should been inside different class
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.rout
                    ) {
                        composable(Screen.Home.rout) {
                            HomeScreen(navController)
                        }

                        //Arguments are defined like this
                        //There is usage at MovieItem.kt 82nth row
                        composable(
                            Screen.Details.rout + "/{movieId}",
                            arguments = listOf(
                                navArgument("movieId") { type = NavType.IntType }
                            )
                        ) {
                            DetailsScreen()
                        }
                    }

                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }


}