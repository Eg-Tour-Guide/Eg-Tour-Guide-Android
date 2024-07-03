package com.egtourguide.user.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun SettingsScreenPreview() {
    EGTourGuideTheme {
        SettingsContent()
    }
}

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        changeNotificationsState = viewModel::changeNotificationsState
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsState = SettingsState(),
    changeNotificationsState: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            showBack = true,
            onBackClicked = {},
            modifier = Modifier.height(52.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_push_notifications),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = stringResource(R.string.push_notifications),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )

            CustomSwitch(
                checked = uiState.pushNotifications,
                onCheckedChange = changeNotificationsState
            )
        }
    }
}