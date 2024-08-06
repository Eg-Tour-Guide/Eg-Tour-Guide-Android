package com.egtourguide.user.presentation.user

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.DataRow
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.presentation.main.components.ArtifactDetectionDialog

@Preview
@Composable
private fun UserScreenRoot() {
    EGTourGuideTheme {
        UserScreenContent()
    }
}

@Composable
fun UserScreenRoot(
    viewModel: UserViewModel = hiltViewModel(),
    navigateToEditProfile: () -> Unit,
    navigateToSaved: () -> Unit,
    navigateToMyTours: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToSettings: () -> Unit,
    onNavigateToDetectedArtifact: (DetectedArtifact) -> Unit,
    navigateToAuth: () -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isDetectionDialogShown by remember { mutableStateOf(false) }

    UserScreenContent(
        navigateToSearch = onNavigateToSearch,
        navigateToEditProfile = navigateToEditProfile,
        navigateToSaved = navigateToSaved,
        navigateToMyTours = navigateToMyTours,
        navigateToChangePassword = navigateToChangePassword,
        navigateToSettings = navigateToSettings,
        onLogoutClicked = {
            viewModel.logout()
            navigateToAuth()
        },
        onCaptureObjectClicked = { isDetectionDialogShown = true }
    )

    LaunchedEffect(key1 = uiState.detectedArtifact) {
        if (uiState.detectedArtifact != null) {
            isDetectionDialogShown = false
            onNavigateToDetectedArtifact(uiState.detectedArtifact!!)
            viewModel.clearDetectionSuccess()
            Toast.makeText(context, uiState.detectedArtifact!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    ArtifactDetectionDialog(
        isShown = isDetectionDialogShown,
        isDetectionLoading = uiState.isDetectionLoading,
        onDismissRequest = { isDetectionDialogShown = false },
        detectArtifact = viewModel::detectArtifact
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
    onLogoutClicked: () -> Unit = {},
    onCaptureObjectClicked: () -> Unit = {}
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
            showCaptureObject = true,
            onSearchClicked = navigateToSearch,
            onCaptureObjectClicked = onCaptureObjectClicked
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