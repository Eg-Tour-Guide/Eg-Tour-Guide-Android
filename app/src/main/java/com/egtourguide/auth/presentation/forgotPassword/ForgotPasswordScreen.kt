package com.egtourguide.auth.presentation.forgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.auth.presentation.components.AuthHeader
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToOTP: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ForgotPasswordScreenContent(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onBackToLoginClicked = onNavigateUp,
        onNextClicked = viewModel::getForgotPasswordCode
    )

    LaunchedEffect(key1 = uiState.isCodeSentSuccessfully) {
        if (uiState.isCodeSentSuccessfully) onNavigateToOTP()
    }
}

@Composable
private fun ForgotPasswordScreenContent(
    uiState: ForgotPasswordUIState,
    onEmailChanged: (String) -> Unit,
    onBackToLoginClicked: () -> Unit,
    onNextClicked: () -> Unit
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
            title = stringResource(R.string.forgot_password)
        )

        ForgotPasswordDataSection(
            email = uiState.email,
            error = uiState.error,
            onEmailChanged = onEmailChanged,
            focusManager = focusManager
        )

        ForgotPasswordFooter(
            focusManager = focusManager,
            onBackToLoginClicked = onBackToLoginClicked,
            onNextClicked = onNextClicked,
            isLoading = uiState.isLoading
        )
    }
}

@Composable
private fun ForgotPasswordDataSection(
    email: String,
    error: String?,
    onEmailChanged: (String) -> Unit,
    focusManager: FocusManager
) {
    Text(
        text = stringResource(R.string.enter_the_email_address_associated),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        errorText = error,
        onValueChanged = onEmailChanged,
        labelText = stringResource(id = R.string.email),
        placeholderText = stringResource(id = R.string.email),
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
private fun ForgotPasswordFooter(
    focusManager: FocusManager,
    onBackToLoginClicked: () -> Unit,
    onNextClicked: () -> Unit,
    isLoading: Boolean
) {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)
        ) {
            append("Back To Login?")
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = { onBackToLoginClicked() },
        style = MaterialTheme.typography.titleMedium
    )

    MainButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        text = stringResource(R.string.next),
        onClick = {
            focusManager.clearFocus()
            onNextClicked()
        },
        isLoading = isLoading,
        isEnabled = !isLoading
    )
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}