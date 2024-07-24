package com.egtourguide.auth.presentation.screens.resetPassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.domain.validation.ValidationCases
import com.egtourguide.auth.presentation.components.AuthHeader
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    EGTourGuideTheme {
        ResetPasswordScreenContent()
    }
}

@Composable
fun ResetPasswordScreen(
    code: String = "",
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ResetPasswordScreenContent(
        uiState = uiState,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onResetPasswordClicked = { viewModel.onResetClicked(code = code) }
    )

    LaunchedEffect(key1 = uiState.isPasswordResetSuccess) {
        if (uiState.isPasswordResetSuccess) {
            onNavigateToLogin()
        }
    }

    LaunchedEffect(key1 = uiState.isError) {
        if (uiState.isError) {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_reset_password_please_try_again),
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearError()
        }
    }

    LaunchedEffect(key1 = uiState.isNetworkError) {
        if (uiState.isNetworkError) {
            Toast.makeText(
                context,
                context.getString(R.string.network_error_toast),
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearNetworkError()
        }
    }
}

@Composable
private fun ResetPasswordScreenContent(
    uiState: ResetPasswordUIState = ResetPasswordUIState(),
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onResetPasswordClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthHeader(
            title = stringResource(R.string.reset_password)
        )

        MainTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.password,
            onValueChanged = onPasswordChanged,
            labelText = stringResource(id = R.string.password),
            placeholderText = stringResource(id = R.string.password),
            imeAction = ImeAction.Next,
            isPassword = true,
            errorText = when (uiState.passwordError) {
                ValidationCases.EMPTY -> stringResource(id = R.string.password_empty_error)
                ValidationCases.ERROR -> stringResource(id = R.string.password_form_error)
                else -> null
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        MainTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.confirmPassword,
            onValueChanged = onConfirmPasswordChanged,
            labelText = stringResource(id = R.string.confirm_password),
            placeholderText = stringResource(id = R.string.confirm_password),
            imeAction = ImeAction.Done,
            isPassword = true,
            errorText = when (uiState.confirmPasswordError) {
                ValidationCases.EMPTY -> stringResource(id = R.string.confirm_password_empty_error)
                ValidationCases.NOT_MATCHED -> stringResource(id = R.string.confirm_password_not_matched_error)
                else -> null
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        MainButton(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.reset_password),
            onClick = {
                focusManager.clearFocus()
                onResetPasswordClicked()
            },
            isLoading = uiState.isLoading,
            isEnabled = !uiState.isLoading
        )
    }
}