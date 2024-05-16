package com.erenalparslan.movieapp.movieList.presentaion.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.erenalparslan.movieapp.movieList.data.remote.MovieApi
import com.erenalparslan.movieapp.movieList.domain.model.Movie
import com.erenalparslan.movieapp.movieList.util.RatingBar
import com.erenalparslan.movieapp.movieList.util.Screen
import com.erenalparslan.movieapp.movieList.util.getAverageColor

//This page create Movie Cards

@Composable
fun MovieItem(
    movie: Movie,
    navHostController: NavHostController
) {
    // Create an AsyncImagePainter with the movie's backdrop image URL
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movie.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    // Define a default color for the background gradient
    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultColor)
    }

    // Main container for the movie item
    Column(
        modifier = Modifier
            .wrapContentHeight() // Set the height to wrap the content
            .width(200.dp) // Set a fixed width for the item
            .padding(8.dp) // Apply padding around the item
            .clip(RoundedCornerShape(28.dp)) // Clip the corners to be rounded
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        dominantColor
                    )
                )
            ) // Apply a vertical gradient background
            .clickable {
                navHostController.navigate(Screen.Details.rout + "/${movie.id}")
            } // Navigate to the movie details screen on click
    ) {
        // Display an error icon if the image fails to load
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Fill the width of the parent
                    .padding(6.dp) // Apply padding around the box
                    .height(250.dp) // Set a fixed height for the box
                    .clip(RoundedCornerShape(22.dp)) // Clip the corners to be rounded
                    .background(MaterialTheme.colorScheme.primaryContainer), // Set background color
                contentAlignment = Alignment.Center // Center the content inside the box
            ) {
                Icon(
                    modifier = Modifier.size(70.dp), // Set the size of the icon
                    imageVector = Icons.Rounded.ImageNotSupported, // Use a "not supported" image icon
                    contentDescription = movie.title // Set content description for accessibility
                )
            }
        }

        // Display the image if it loads successfully
        if (imageState is AsyncImagePainter.State.Success) {
            // Update the dominant color based on the image's average color
            dominantColor = getAverageColor(
                imageBitmap = imageState.result.drawable.toBitmap().asImageBitmap()
            )

            // Display the image
            Image(
                modifier = Modifier
                    .fillMaxWidth() // Fill the width of the parent
                    .padding(6.dp) // Apply padding around the image
                    .height(250.dp) // Set a fixed height for the image
                    .clip(RoundedCornerShape(22.dp)), // Clip the corners to be rounded
                painter = imageState.painter, // Use the painter from the image state
                contentDescription = movie.title, // Set content description for accessibility
                contentScale = ContentScale.Crop // Crop the image to fill the bounds
            )
        }

        Spacer(modifier = Modifier.height(6.dp)) // Add vertical space

        // Display the movie title
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp), // Apply horizontal padding
            text = movie.title, // Set the text to the movie title
            color = Color.White, // Set the text color to white
            fontSize = 15.sp, // Set the font size
            maxLines = 1 // Limit the text to one line
        )

        // Display the movie rating
        Row(
            modifier = Modifier
                .fillMaxWidth() // Fill the width of the parent
                .padding(start = 16.dp, bottom = 12.dp, top = 4.dp) // Apply padding
        ) {
            RatingBar(
                starsModifier = Modifier.size(18.dp), // Set the size of the stars
                rating = movie.vote_average / 2 // Set the rating (dividing by 2 to fit a 5-star system)
            )

            Text(
                modifier = Modifier.padding(start = 4.dp), // Apply padding to the start
                text = movie.vote_average.toString()
                    .take(3), // Display the vote average (taking the first 3 characters)
                color = Color.LightGray, // Set the text color to light gray
                fontSize = 14.sp, // Set the font size
                maxLines = 1 // Limit the text to one line
            )
        }
    }
}


