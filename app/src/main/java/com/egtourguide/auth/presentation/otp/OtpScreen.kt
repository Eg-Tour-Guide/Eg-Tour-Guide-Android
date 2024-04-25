package com.egtourguide.auth.presentation.otp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Preview(showBackground = true)
@Composable
private fun OtpScreenPreview() {
    EGTourGuideTheme {
        OtpContent(
            uiState = OtpUIState()
        )
    }
}

@Composable
fun OtpScreen(
    viewModel: OtpViewModel = hiltViewModel(),
    code: String,
    fromSignup: Boolean,
    name: String,
    email: String,
    phone: String,
    password: String,
    onNavigateToResetPassword: (String) -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    OtpContent(
        uiState = uiState,
        onCodeChanged = viewModel::changeCode,
        onVerifyClicked = { viewModel.onVerifyClicked(sentCode = code) }
    )

    LaunchedEffect(key1 = uiState.isVerifiedSuccessfully) {
        if (uiState.isVerifiedSuccessfully) {
            viewModel.clearVerifySuccess()

            if (fromSignup) {
                viewModel.signup(name, email, phone, password)
            } else {
                onNavigateToResetPassword(code)
            }
        }
    }

    LaunchedEffect(key1 = uiState.isSignedSuccessfully) {
        if (uiState.isSignedSuccessfully) {
            viewModel.clearSignSuccess()
            onNavigateToHome()
        }
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun OtpContent(
    uiState: OtpUIState,
    onCodeChanged: (String) -> Unit = {},
    onVerifyClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

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
            title = stringResource(id = R.string.verification)
        )

        OtpDataSection(
            code = uiState.code,
            onCodeChanged = onCodeChanged,
            codeError = uiState.codeError,
            isLoading = uiState.isLoading
        )

        OtpFooter(
            onVerifyClicked = onVerifyClicked,
            isLoading = uiState.isLoading
        )
    }
}

@Composable
private fun OtpDataSection(
    code: String,
    onCodeChanged: (String) -> Unit,
    codeError: ValidationCases,
    isLoading: Boolean
) {
    val focusManager = LocalFocusManager.current

    Text(
        text = stringResource(id = R.string.enter_verification_code_we_sent),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = code,
        isEnabled = !isLoading,
        onValueChanged = onCodeChanged,
        labelText = stringResource(id = R.string.verification_code),
        placeholderText = stringResource(id = R.string.enter_verification_code),
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        errorText = when (codeError) {
            ValidationCases.EMPTY -> stringResource(id = R.string.code_empty_error)
            ValidationCases.NOT_MATCHED -> stringResource(id = R.string.code_not_matched_error)
            else -> null
        }
    )
}

@Composable
private fun OtpFooter(
    onVerifyClicked: () -> Unit,
    isLoading: Boolean
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)
        ) {
            append(stringResource(id = R.string.didn_t_receive_code))
        }

        append(" ")

        pushStringAnnotation(tag = "resend", annotation = "resend")

        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.outlineVariant)
        ) {
            append(stringResource(id = R.string.resend_code))
        }

        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "resend",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                Toast.makeText(
                    context,
                    context.getString(R.string.we_sent_new_code),
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        style = MaterialTheme.typography.titleMedium
    )

    MainButton(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .height(56.dp),
        text = stringResource(id = R.string.verify),
        onClick = {
            focusManager.clearFocus()
            onVerifyClicked()
        },
        isLoading = isLoading,
        isEnabled = !isLoading
    )
}