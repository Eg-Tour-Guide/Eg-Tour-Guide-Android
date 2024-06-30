package com.egtourguide.auth.presentation.screens.forgotPassword

import android.widget.Toast
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
import com.egtourguide.auth.domain.validation.ValidationCases
import com.egtourguide.auth.presentation.components.AuthHeader
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToOTP: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ForgotPasswordScreenContent(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onBackToLoginClicked = onNavigateUp,
        onNextClicked = viewModel::onNextClicked
    )

    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(key1 = uiState.isCodeSentSuccessfully) {
        if (uiState.isCodeSentSuccessfully) {
            uiState.successMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            onNavigateToOTP(uiState.code, uiState.email)
            viewModel.clearSuccess()
        }
    }
}

@Composable
private fun ForgotPasswordScreenContent(
    uiState: ForgotPasswordUIState,
    onEmailChanged: (String) -> Unit = {},
    onBackToLoginClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {}
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
            emailError = uiState.emailError,
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
    emailError: ValidationCases,
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
        onValueChanged = onEmailChanged,
        labelText = stringResource(id = R.string.email),
        placeholderText = stringResource(id = R.string.email),
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        errorText = when (emailError) {
            ValidationCases.EMPTY -> stringResource(id = R.string.email_empty_error)
            ValidationCases.ERROR -> stringResource(id = R.string.email_form_error)
            else -> null
        }
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
            append(stringResource(id = R.string.remembered_your_password))
        }

        append(" ")

        pushStringAnnotation(tag = "login", annotation = "login")

        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.outlineVariant)
        ) {
            append(stringResource(id = R.string.login_now))
        }

        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "login",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onBackToLoginClicked()
            }
        },
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
    EGTourGuideTheme {
        ForgotPasswordScreenContent(
            uiState = ForgotPasswordUIState()
        )
    }
}