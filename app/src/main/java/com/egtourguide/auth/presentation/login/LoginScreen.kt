package com.egtourguide.auth.presentation.login

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
import com.egtourguide.auth.presentation.components.AuthHeader
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgetPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::changeEmail,
        onPasswordChange = viewModel::changePassword,
        onLoginClicked = viewModel::loginClick,
        onNavigateToSignUp = onNavigateToSignUp,
        onNavigateToForgetPassword = onNavigateToForgetPassword
    )

    LaunchedEffect(key1 = uiState.isSuccess, key2 = uiState.isError) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            onNavigateToHome()
        } else if (uiState.isError) {
            Toast.makeText(context, "wrong email or password", Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@Composable
fun LoginContent(
    uiState: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgetPassword: () -> Unit
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
            title = stringResource(id = R.string.welcome_Back)
        )

        LoginDataSection(
            focusManager = focusManager,
            email = uiState.email,
            password = uiState.password,
            isLoading = uiState.isLoading,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClicked = onLoginClicked,
            onNavigateToForgetPassword = onNavigateToForgetPassword
        )

        LoginFooter(onNavigateToSignUp)
    }
}

@Composable
private fun LoginDataSection(
    focusManager: FocusManager,
    email: String,
    password: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onNavigateToForgetPassword: () -> Unit
) {
    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChanged = onEmailChange,
        labelText = stringResource(id = R.string.email),
        placeholderText = stringResource(id = R.string.enter_your_email),
        keyboardType = KeyboardType.Email
    )

    MainTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChanged = onPasswordChange,
        labelText = stringResource(id = R.string.password),
        placeholderText = stringResource(id = R.string.enter_your_password),
        keyboardType = KeyboardType.Password,
        isPassword = true,
        imeAction = ImeAction.Done,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )

    ClickableText(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.forgot_password))
        },
        onClick = { onNavigateToForgetPassword() },
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )

    MainButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        text = stringResource(id = R.string.login),
        onClick = {
            focusManager.clearFocus()
            onLoginClicked()
        },
        isLoading = isLoading
    )

}

@Composable
private fun LoginFooter(onNavigateToSignUp: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)
        ) {
            append(stringResource(id = R.string.dont_have_account))
        }

        append(" ")

        pushStringAnnotation(tag = "register", annotation = "register")

        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.outlineVariant)
        ) {
            append(stringResource(id = R.string.regster_now))
        }

        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "register",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onNavigateToSignUp()
            }
        },
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    EGTourGuideTheme {
        LoginScreen(
            onNavigateToSignUp = {},
            onNavigateToForgetPassword = {},
            onNavigateToHome = {}
        )
    }
}