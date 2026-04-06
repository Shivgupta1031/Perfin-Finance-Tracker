package com.devshiv.perfin.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.draw.paint
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devshiv.perfin.R
import com.devshiv.perfin.ui.navigation.Routes
import kotlinx.coroutines.delay
import com.devshiv.perfin.ui.theme.DarkGreen
import com.devshiv.perfin.ui.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    vm: SplashViewModel = hiltViewModel()
) {

    val destination by vm.startDestination.collectAsState(initial = null)

    var animationDone by remember { mutableStateOf(false) }
    var navigated by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(destination, animationDone) {
        if (!navigated && destination != null && animationDone) {
            navigated = true
            navController.navigate(destination!!) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        animationDone = true
    }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = tween(800, easing = FastOutSlowInEasing)
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.screen_bg),
                contentScale = ContentScale.Crop
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "Mascot",
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
                .alpha(alpha)
                .offset(y = translateY.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn(animationSpec = tween(800)) +
                    slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Text(
                text = "PerFin",
                style = typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = DarkGreen
            )
        }
    }
}

@Composable
@Preview
fun SplashPreview() {
    SplashScreen(navController = rememberNavController())
}