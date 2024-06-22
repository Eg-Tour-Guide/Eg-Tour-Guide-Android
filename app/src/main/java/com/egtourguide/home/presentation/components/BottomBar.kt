package com.egtourguide.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.egtourguide.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedScreen: BottomBarScreens,
    onItemSelected: (BottomBarScreens) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomBarItem(
            iconSelected = R.drawable.ic_home_selected,
            iconUnselected = R.drawable.ic_home_unselected,
            title = "Home",
            isSelected = selectedScreen == BottomBarScreens.Home,
            onItemClicked = {
                onItemSelected(BottomBarScreens.Home)
            }
        )
        BottomBarItem(
            iconSelected = R.drawable.ic_tours_selected,
            iconUnselected = R.drawable.ic_tours_unselected,
            title = "Tours",
            isSelected = selectedScreen == BottomBarScreens.Tours,
            onItemClicked = {
                onItemSelected(BottomBarScreens.Tours)
            }
        )
        BottomBarItem(
            iconSelected = R.drawable.ic_landmarks_selected,
            iconUnselected = R.drawable.ic_landmarks_unselected,
            title = "Landmarks",
            isSelected = selectedScreen == BottomBarScreens.Landmarks,
            onItemClicked = {
                onItemSelected(BottomBarScreens.Landmarks)
            }
        )
        BottomBarItem(
            iconSelected = R.drawable.ic_artifacts_selected,
            iconUnselected = R.drawable.ic_artifacts_unselected,
            title = "Artifacts",
            isSelected = selectedScreen == BottomBarScreens.Artifacts,
            onItemClicked = {
                onItemSelected(BottomBarScreens.Artifacts)
            }
        )
        BottomBarItem(
            iconSelected = R.drawable.ic_user_selected,
            iconUnselected = R.drawable.ic_user_unselected,
            title = "User",
            isSelected = selectedScreen == BottomBarScreens.User,
            onItemClicked = {
                onItemSelected(BottomBarScreens.User)
            }
        )
    }
}

@Composable
fun BottomBarItem(
    iconSelected: Int,
    iconUnselected: Int,
    title: String,
    isSelected: Boolean = false,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onItemClicked()
            },
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = if (isSelected) iconSelected else iconUnselected),
            contentDescription = "Icon"
        )
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = if (isSelected) MaterialTheme.typography.bodySmall else MaterialTheme.typography.labelLarge
        )
    }
}

enum class BottomBarScreens {
    Home,
    Tours,
    Landmarks,
    Artifacts, User
}

