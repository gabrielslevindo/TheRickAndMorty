package com.example.therickandmorty.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.example.therickandmorty.R
import kotlinx.coroutines.delay

object SplashScreen : Screen {
    @Composable
    override fun Content() {
        var startAnimation by remember { mutableStateOf(false) }
        var navigateToMain by remember { mutableStateOf(false) }

        // Fade-in do logo
        val alphaAnim by animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 1000)
        )

        LaunchedEffect(Unit) {
            startAnimation = true
            delay(2000)
            navigateToMain = true
        }

        if (navigateToMain) {
            Navigator(MainScreen)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rick),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(250.dp)
                        .alpha(alphaAnim)
                )
            }
        }
    }
}