package com.egtourguide.home.presentation.screens.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.navigation.AppGraph
import com.egtourguide.core.presentation.navigation.AppScreen

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedScreen: String,
    onItemSelected: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarScreen.values().forEach { screen ->
            BottomBarItem(
                item = screen,
                isSelected = selectedScreen == screen.route,
                onItemClicked = { onItemSelected(screen.route) }
            )
        }
    }
}

@Composable
fun BottomBarItem(
    item: BottomBarScreen,
    onItemClicked: () -> Unit,
    isSelected: Boolean = false
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
            painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(id = item.title),
            color = MaterialTheme.colorScheme.onBackground,
            style = if (isSelected) MaterialTheme.typography.bodySmall else MaterialTheme.typography.labelLarge
        )
    }
}

enum class BottomBarScreen(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int
) {
    Home(
        route = AppScreen.Home.route,
        title = R.string.home,
        unselectedIcon = R.drawable.ic_home_unselected,
        selectedIcon = R.drawable.ic_home_selected
    ),
    Tours(
        route = AppScreen.ToursList.route,
        title = R.string.tours,
        unselectedIcon = R.drawable.ic_tours_unselected,
        selectedIcon = R.drawable.ic_tours_selected
    ),
    Landmarks(
        route = AppScreen.LandmarksList.route,
        title = R.string.landmarks,
        unselectedIcon = R.drawable.ic_landmarks_unselected,
        selectedIcon = R.drawable.ic_landmarks_selected
    ),
    Artifacts(
        route = AppScreen.ArtifactsList.route,
        title = R.string.artifacts,
        unselectedIcon = R.drawable.ic_artifacts_unselected,
        selectedIcon = R.drawable.ic_artifacts_selected
    ),
    User(
        route = AppGraph.User.route,
        title = R.string.user,
        unselectedIcon = R.drawable.ic_user_unselected,
        selectedIcon = R.drawable.ic_user_selected
    )
}
