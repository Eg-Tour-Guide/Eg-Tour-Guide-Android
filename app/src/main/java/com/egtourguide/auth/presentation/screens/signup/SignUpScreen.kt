package com.egtourguide.auth.presentation.screens.signup

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
import androidx.compose.ui.text.input.KeyboardType
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
private fun SignUpScreenPreview() {
    EGTourGuideTheme {
        SignUpContent(
            uiState = SignUpUIState()
        )
    }
}

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToOTP: (String, String, String, String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    SignUpContent(
        uiState = uiState,
        onNavigateToLogin = onNavigateToLogin,
        onNameChanged = viewModel::changeName,
        onPhoneChanged = viewModel::changePhone,
        onEmailChanged = viewModel::changeEmail,
        onPasswordChanged = viewModel::changePassword,
        onConfirmPasswordChanged = viewModel::changeConfirmPassword,
        onRegisterClicked = viewModel::onRegisterClicked
    )

    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToOTP(
                uiState.code,
                uiState.name,
                uiState.email,
                uiState.phone,
                uiState.password
            )
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@Composable
private fun SignUpContent(
    uiState: SignUpUIState,
    onNavigateToLogin: () -> Unit = {},
    onNameChanged: (String) -> Unit = {},
    onPhoneChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onRegisterClicked: () -> Unit = {}
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
            title = stringResource(id = R.string.hello_let_s_get_started)
        )

        SignUpDataSection(
            focusManager = focusManager,
            name = uiState.name,
            phone = uiState.phone,
            email = uiState.email,
            password = uiState.password,
            confirmPassword = uiState.confirmPassword,
            isLoading = uiState.isLoading,
            onNameChanged = onNameChanged,
            onPhoneChanged = onPhoneChanged,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onConfirmPasswordChanged = onConfirmPasswordChanged,
            onRegisterClicked = onRegisterClicked,
            nameError = uiState.nameError,
            emailError = uiState.emailError,
            phoneError = uiState.phoneError,
            passwordError = uiState.passwordError,
            confirmPasswordError = uiState.confirmPasswordError
        )

        SignUpFooter(onNavigateToLogin)
    }
}

@Composable
private fun SignUpDataSection(
    focusManager: FocusManager,
    name: String,
    phone: String,
    email: String,
    password: String,
    isLoading: Boolean,
    confirmPassword: String,
    onNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    nameError: ValidationCases,
    emailError: ValidationCases,
    phoneError: ValidationCases,
    passwordError: ValidationCases,
    confirmPasswordError: ValidationCases
) {
    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChanged = onNameChanged,
        labelText = stringResource(id = R.string.name),
        placeholderText = stringResource(id = R.string.enter_your_name),
        errorText = when (nameError) {
            ValidationCases.EMPTY -> stringResource(R.string.name_empty_error)
            ValidationCases.ERROR -> stringResource(R.string.name_form_error)
            else -> null
        }
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = phone,
        onValueChanged = onPhoneChanged,
        labelText = stringResource(id = R.string.phone),
        placeholderText = stringResource(id = R.string.enter_your_phone),
        keyboardType = KeyboardType.Phone,
        errorText = when (phoneError) {
            ValidationCases.EMPTY -> stringResource(id = R.string.phone_empty_error)
            ValidationCases.ERROR -> stringResource(id = R.string.phone_form_error)
            else -> null
        }
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChanged = onEmailChanged,
        labelText = stringResource(id = R.string.email),
        placeholderText = stringResource(id = R.string.enter_your_email),
        keyboardType = KeyboardType.Email,
        errorText = when (emailError) {
            ValidationCases.EMPTY -> stringResource(id = R.string.email_empty_error)
            ValidationCases.ERROR -> stringResource(id = R.string.email_form_error)
            else -> null
        }
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChanged = onPasswordChanged,
        labelText = stringResource(id = R.string.password),
        placeholderText = stringResource(id = R.string.enter_your_password),
        isPassword = true,
        keyboardType = KeyboardType.Password,
        errorText = when (passwordError) {
            ValidationCases.EMPTY -> stringResource(id = R.string.password_empty_error)
            ValidationCases.ERROR -> stringResource(id = R.string.password_form_error)
            else -> null
        }
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = confirmPassword,
        onValueChanged = onConfirmPasswordChanged,
        labelText = stringResource(id = R.string.confirm_password),
        placeholderText = stringResource(id = R.string.reenter_your_password),
        isPassword = true,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        errorText = when (confirmPasswordError) {
            ValidationCases.EMPTY -> stringResource(id = R.string.confirm_password_empty_error)
            ValidationCases.NOT_MATCHED -> stringResource(id = R.string.confirm_password_not_matched_error)
            else -> null
        }
    )

    MainButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        text = stringResource(id = R.string.register),
        onClick = {
            focusManager.clearFocus()
            onRegisterClicked()
        },
        isLoading = isLoading,
        isEnabled = !isLoading
    )
}

@Composable
private fun SignUpFooter(onNavigateToLogin: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)
        ) {
            append(stringResource(id = R.string.already_have_an_account))
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
                onNavigateToLogin()
            }
        },
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}