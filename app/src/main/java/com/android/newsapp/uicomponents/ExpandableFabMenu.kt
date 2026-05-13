package com.android.newsapp.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableFabMenu(
    modifier: Modifier = Modifier,
    onShareClick: () -> Unit,
    openInBrowser: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(targetValue = if (isExpanded) 45f else 0f)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { 50 })
        ) {
            SmallFloatingActionButton(
                onClick = {
                    isExpanded = false
                    onShareClick()
                }
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { 50 })
        ) {
            SmallFloatingActionButton(
                onClick = {
                    isExpanded = false
                    openInBrowser()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = "Open In"
                )
            }
        }

        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Menu",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}