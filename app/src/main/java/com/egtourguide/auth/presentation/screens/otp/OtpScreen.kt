package com.egtourguide.auth.presentation.screens.otp

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
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.egtourguide.R
import com.egtourguide.core.domain.validation.ValidationCases
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
    val lifecycleOwner = LocalLifecycleOwner.current

    OtpContent(
        uiState = uiState,
        onCodeChanged = viewModel::changeCode,
        onVerifyClicked = viewModel::onVerifyClicked,
        onResendClicked = {
            viewModel.resendCode(fromSignup = fromSignup)
            Toast.makeText(
                context,
                context.getString(R.string.a_new_verification_code_has_been_sent_to_your_email),
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.saveData(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    sentCode = code
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = uiState.isVerifiedSuccessfully) {
        if (uiState.isVerifiedSuccessfully) {
            viewModel.clearVerifySuccess()

            if (fromSignup) {
                viewModel.signup()
            } else {
                onNavigateToResetPassword(code)
            }
        }
    }

    LaunchedEffect(key1 = uiState.isSignedSuccessfully) {
        if (uiState.isSignedSuccessfully) {
            onNavigateToHome()
        }
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
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
private fun OtpContent(
    uiState: OtpUIState,
    onCodeChanged: (String) -> Unit = {},
    onVerifyClicked: () -> Unit = {},
    onResendClicked: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthHeader(
            title = stringResource(id = R.string.verification)
        )

        Text(
            text = stringResource(id = R.string.enter_verification_code_we_sent),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        MainTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.code,
            isEnabled = !uiState.isLoading,
            onValueChanged = onCodeChanged,
            labelText = stringResource(id = R.string.verification_code),
            placeholderText = stringResource(id = R.string.enter_verification_code),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            errorText = when (uiState.codeError) {
                ValidationCases.EMPTY -> stringResource(id = R.string.code_empty_error)
                ValidationCases.NOT_MATCHED -> stringResource(id = R.string.code_not_matched_error)
                else -> null
            }
        )

        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                append(stringResource(id = R.string.didn_t_receive_code))
            }

            append(" ")

            pushStringAnnotation(tag = "resend", annotation = "resend")

            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outlineVariant)) {
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
                    onResendClicked()
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
            isLoading = uiState.isLoading,
            isEnabled = !uiState.isLoading
        )
    }
}