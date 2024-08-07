package com.egtourguide.user.presentation.editProfile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
private fun EditProfileScreenPreview() {
    EGTourGuideTheme {
        EditProfileContent()
    }
}

@Composable
fun EditProfileScreenRoot(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.profile_updated_successfully),
                Toast.LENGTH_SHORT
            ).show()
            onBackClicked()
        }
    }

    LaunchedEffect(key1 = uiState.isError) {
        if (uiState.isError) {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_update_profile_please_try_again),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearError()
        }
    }

    EditProfileContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onNameChanged = viewModel::onNameChanged,
        onPhoneChanged = viewModel::onPhoneChanged,
        onEmailChanged = viewModel::onEmailChanged,
        onSaveClicked = viewModel::onSaveClicked
    )
}

@Composable
private fun EditProfileContent(
    uiState: EditProfileState = EditProfileState(),
    onBackClicked: () -> Unit = {},
    onNameChanged: (String) -> Unit = {},
    onPhoneChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
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
            onBackClicked = onBackClicked,
            modifier = Modifier.height(52.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user_placeholder),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize()
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_change_picture),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 7.dp)
                        .align(Alignment.BottomEnd)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                        .size(24.dp)
                )
            }

            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = uiState.name,
                onValueChanged = onNameChanged,
                labelText = stringResource(id = R.string.name),
                placeholderText = stringResource(id = R.string.enter_your_name),
                errorText = when (uiState.nameError) {
                    ValidationCases.EMPTY -> stringResource(R.string.name_empty_error)
                    ValidationCases.ERROR -> stringResource(R.string.name_form_error)
                    else -> null
                }
            )

            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = uiState.phone,
                onValueChanged = onPhoneChanged,
                labelText = stringResource(id = R.string.phone),
                placeholderText = stringResource(id = R.string.enter_your_phone),
                keyboardType = KeyboardType.Phone,
                errorText = when (uiState.phoneError) {
                    ValidationCases.EMPTY -> stringResource(id = R.string.phone_empty_error)
                    ValidationCases.ERROR -> stringResource(id = R.string.phone_form_error)
                    else -> null
                }
            )

            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = uiState.email,
                onValueChanged = onEmailChanged,
                labelText = stringResource(id = R.string.email),
                placeholderText = stringResource(id = R.string.enter_your_email),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                errorText = when (uiState.emailError) {
                    ValidationCases.EMPTY -> stringResource(id = R.string.email_empty_error)
                    ValidationCases.ERROR -> stringResource(id = R.string.email_form_error)
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