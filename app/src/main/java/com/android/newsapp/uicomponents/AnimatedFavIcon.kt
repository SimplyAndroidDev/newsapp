package com.android.newsapp.uicomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AnimatedFavIcon(modifier: Modifier = Modifier, isFav: Boolean, toggleFavState: () -> Unit) {
    val animScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Icon(
        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = "favourite",
        modifier = modifier
            .size(16.dp)
            .scale(animScale.value)
            .clickable {
                scope.launch {
                    // Animation Sequence: Scale down -> Change State -> Scale up
                    animScale.animateTo(0.7f, tween(100))
                    toggleFavState()
                    animScale.animateTo(
                        1.2f,
                        spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                    animScale.animateTo(1f, spring())
                }
            }
    )
}