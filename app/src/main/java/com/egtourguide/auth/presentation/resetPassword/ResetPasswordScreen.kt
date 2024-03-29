package com.egtourguide.auth.presentation.resetPassword

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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.auth.presentation.components.AuthHeader
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField

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
        onResetPasswordClicked = { viewModel.onResetClicked(context = context, code = code) }
    )

    LaunchedEffect(key1 = uiState.isPasswordResetSuccess) {
        if (uiState.isPasswordResetSuccess) onNavigateToLogin()
    }
}

@Composable
private fun ResetPasswordScreenContent(
    uiState: ResetPasswordUIState,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onResetPasswordClicked: () -> Unit
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

        ResetPasswordDataSection(
            password = uiState.password,
            confirmPassword = uiState.confirmPassword,
            passwordError = uiState.passwordError,
            confirmPasswordError = uiState.confirmPasswordError,
            onPasswordChanged = onPasswordChanged,
            onConfirmPasswordChanged = onConfirmPasswordChanged,
            focusManager = focusManager
        )

        ResetPasswordFooter(
            focusManager = focusManager,
            onResetPasswordClicked = onResetPasswordClicked,
            isLoading = uiState.isLoading
        )
    }
}

@Composable
private fun ResetPasswordDataSection(
    password: String,
    confirmPassword: String,
    passwordError: String?,
    confirmPasswordError: String?,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    focusManager: FocusManager
) {
    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChanged = onPasswordChanged,
        labelText = stringResource(id = R.string.password),
        placeholderText = stringResource(id = R.string.password),
        imeAction = ImeAction.Next,
        isPassword = true,
        errorText = passwordError,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = confirmPassword,
        onValueChanged = onConfirmPasswordChanged,
        labelText = stringResource(id = R.string.confirm_password),
        placeholderText = stringResource(id = R.string.confirm_password),
        imeAction = ImeAction.Done,
        isPassword = true,
        errorText = confirmPasswordError,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
private fun ResetPasswordFooter(
    focusManager: FocusManager,
    onResetPasswordClicked: () -> Unit,
    isLoading: Boolean
) {
    MainButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        text = stringResource(id = R.string.reset_password),
        onClick = {
            focusManager.clearFocus()
            onResetPasswordClicked()
        },
        isLoading = isLoading,
        isEnabled = !isLoading
    )
}

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
}

