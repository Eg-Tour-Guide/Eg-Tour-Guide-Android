package com.egtourguide.user.presentation.changePassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.egtourguide.R
import com.egtourguide.core.domain.validation.ValidationCases
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun ChangePasswordScreenPreview() {
    EGTourGuideTheme {
        ChangePasswordContent()
    }
}

@Composable
fun ChangePasswordScreenRoot(
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.data_changed_successfully),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    ChangePasswordContent(
        uiState = uiState,
        onOldPasswordChanged = viewModel::onOldPasswordChanged,
        onNewPasswordChanged = viewModel::onNewPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onSaveClicked = viewModel::onSaveClicked
    )
}

@Composable
private fun ChangePasswordContent(
    uiState: ChangePasswordState = ChangePasswordState(),
    onOldPasswordChanged: (String) -> Unit = {},
    onNewPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onSaveClicked: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

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

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                value = uiState.oldPassword,
                onValueChanged = onOldPasswordChanged,
                labelText = stringResource(id = R.string.old_password),
                placeholderText = stringResource(id = R.string.enter_your_old_password),
                isPassword = true,
                keyboardType = KeyboardType.Password,
                errorText = when (uiState.newPasswordError) {
                    ValidationCases.EMPTY -> stringResource(id = R.string.password_empty_error)
                    ValidationCases.ERROR -> stringResource(id = R.string.password_form_error)
                    else -> null
                }
            )

            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = uiState.newPassword,
                onValueChanged = onNewPasswordChanged,
                labelText = stringResource(id = R.string.new_password),
                placeholderText = stringResource(id = R.string.enter_your_new_password),
                isPassword = true,
                keyboardType = KeyboardType.Password,
                errorText = when (uiState.newPasswordError) {
                    ValidationCases.EMPTY -> stringResource(id = R.string.old_password_empty_error)
                    ValidationCases.ERROR -> stringResource(id = R.string.password_form_error)
                    else -> null
                }
            )

            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = uiState.confirmPassword,
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
                errorText = when (uiState.confirmPasswordError) {
                    ValidationCases.EMPTY -> stringResource(id = R.string.confirm_password_empty_error)
                    ValidationCases.NOT_MATCHED -> stringResource(id = R.string.confirm_password_not_matched_error)
                    else -> null
                }
            )

            MainButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                text = stringResource(id = R.string.save),
                onClick = {
                    focusManager.clearFocus()
                    onSaveClicked()
                },
                isLoading = uiState.isLoading,
                isEnabled = !uiState.isLoading
            )
        }
    }
}