package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devshiv.perfin.ui.theme.DarkGreen
import com.devshiv.perfin.ui.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import com.devshiv.perfin.R
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.ui.theme.TextSecondary
import com.devshiv.perfin.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    settingsVM: SettingsViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { viewModel.pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.screen_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->

                val item = viewModel.pages[page]

                val pageOffset = (
                        (pagerState.currentPage - page) +
                                pagerState.currentPageOffsetFraction
                        )

                val scale = lerp(0.85f, 1f, 1f - kotlin.math.abs(pageOffset))
                val alpha = lerp(0.5f, 1f, 1f - kotlin.math.abs(pageOffset))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                        color = DarkGreen
                    )

                    Image(
                        painter = painterResource(item.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(400.dp)
                            .graphicsLayer {
                                translationX = pageOffset * 80f
                            }
                    )

                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                repeat(viewModel.pages.size) { index ->
                    val isSelected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 6.dp)
                            .background(
                                color = if (isSelected) PrimaryGreen else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage == viewModel.pages.lastIndex) {
                        settingsVM.setOnboardingShown()
                        onFinish()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    if (pagerState.currentPage == 2) "Get Started" else "Next"
                )
            }
        }
    }
}