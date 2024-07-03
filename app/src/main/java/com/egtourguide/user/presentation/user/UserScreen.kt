package com.egtourguide.user.presentation.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.components.DataRow
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun UserScreenRoot() {
    EGTourGuideTheme {
        UserScreenContent()
    }
}

@Composable
fun UserScreenRoot(
    navigateToEditProfile: () -> Unit,
    navigateToSaved: () -> Unit,
    navigateToMyTours: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToSettings: () -> Unit
) {
    UserScreenContent(
        navigateToSearch = {
            // TODO: Here!!
        },
        navigateToEditProfile = navigateToEditProfile,
        navigateToSaved = navigateToSaved,
        navigateToMyTours = navigateToMyTours,
        navigateToChangePassword = navigateToChangePassword,
        navigateToSettings = navigateToSettings,
        onLogoutClicked = {
            // TODO: Here!!
        }
    )
}

@Composable
private fun UserScreenContent(
    navigateToSearch: () -> Unit = {},
    navigateToEditProfile: () -> Unit = {},
    navigateToSaved: () -> Unit = {},
    navigateToMyTours: () -> Unit = {},
    navigateToChangePassword: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    onLogoutClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            modifier = Modifier.height(61.dp),
            showLogo = true,
            showSearch = true,
            showNotifications = true,
            // TODO: Implement notifications, active tour, and active tour logic!!
//            showNotificationsBadge = true,
            showActiveTour = true,
            showCaptureObject = true,
            onSearchClicked = navigateToSearch,
            onCaptureObjectClicked = {
                // TODO: Here!!
            },
            onNotificationsClicked = {
                // TODO: Here!!
            },
            onActiveTourClicked = {
                // TODO: And here!!
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_user_placeholder),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 32.dp)
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Abdo Sharaf",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 32.dp)
                .fillMaxWidth()
        )

        DataRow(
            icon = R.drawable.ic_edit_profile,
            iconSize = 20.dp,
            iconPadding = 16.dp,
            textStyle = MaterialTheme.typography.displaySmall,
            text = stringResource(id = R.string.edit_profile),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { navigateToEditProfile() }
                .padding(vertical = 8.dp)
        )

        DataRow(
            icon = R.drawable.ic_save,
            iconSize = 20.dp,
            iconPadding = 16.dp,
            textStyle = MaterialTheme.typography.displaySmall,
            text = stringResource(id = R.string.saved),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { navigateToSaved() }
                .padding(vertical = 8.dp)
        )

        DataRow(
            icon = R.drawable.ic_active_tour,
            iconSize = 20.dp,
            iconPadding = 16.dp,
            textStyle = MaterialTheme.typography.displaySmall,
            text = stringResource(id = R.string.my_tours),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { navigateToMyTours() }
                .padding(vertical = 8.dp)
        )

        DataRow(
            icon = R.drawable.ic_settings,
            iconSize = 20.dp,
            iconPadding = 16.dp,
            textStyle = MaterialTheme.typography.displaySmall,
            text = stringResource(id = R.string.settings),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { navigateToSettings() }
                .padding(vertical = 8.dp)
        )

        DataRow(
            icon = R.drawable.ic_change_password,
            iconSize = 20.dp,
            iconPadding = 16.dp,
            textStyle = MaterialTheme.typography.displaySmall,
            text = stringResource(id = R.string.change_password),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { navigateToChangePassword() }
                .padding(vertical = 8.dp)
        )

        DataRow(
            icon = R.drawable.ic_logout,
            iconSize = 20.dp,
            iconPadding = 16.dp,
            iconTint = MaterialTheme.colorScheme.error,
            text = stringResource(id = R.string.logout),
            textStyle = MaterialTheme.typography.displaySmall,
            textColor = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { onLogoutClicked() }
                .padding(vertical = 8.dp)
        )
    }
}