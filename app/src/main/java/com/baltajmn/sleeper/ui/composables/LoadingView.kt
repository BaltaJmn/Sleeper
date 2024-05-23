package com.baltajmn.sleeper.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.baltajmn.sleeper.R

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LottieImage(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.Center),
            animation = R.raw.zzz
        )
    }
}

@Composable
fun LottieImage(
    modifier: Modifier = Modifier,
    animation: Int,
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
    val animationProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = false,
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { animationProgress },
        contentScale = ContentScale.Crop
    )

}