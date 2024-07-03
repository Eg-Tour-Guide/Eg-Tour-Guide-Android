package com.egtourguide.customTours.presentation.createTour

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.egtourguide.R
import com.egtourguide.core.domain.validation.ValidationCases
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.components.ScreenHeader
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview
@Composable
private fun CreateTourScreenPreview() {
    EGTourGuideTheme {
        CreateTourContent()
    }
}

@Composable
fun CreateTourScreenRoot(
    viewModel: CreateTourViewModel = hiltViewModel(),
    isCreate: Boolean = true,
    tourId: String,
    name: String,
    description: String,
    onNavigateBack: () -> Unit,
    navigateToMyTours: () -> Unit,
    navigateToExpanded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE && !isCreate) {
                viewModel.setData(tourId = tourId, name = name, description = description)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = uiState.isCreateSuccess) {
        if (uiState.isCreateSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.created_successfully),
                Toast.LENGTH_SHORT
            ).show()
            navigateToMyTours()
        }
    }

    LaunchedEffect(key1 = uiState.isEditSuccess) {
        if (uiState.isEditSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.edited_successfully),
                Toast.LENGTH_SHORT
            ).show()
            navigateToExpanded()
        }
    }

    CreateTourContent(
        uiState = uiState,
        isCreate = isCreate,
        onNavigateBack = onNavigateBack,
        onNameChanged = viewModel::onNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onSaveClicked = if (isCreate) viewModel::onCreateClicked else viewModel::onSaveClicked
    )
}

@Composable
private fun CreateTourContent(
    uiState: CreateTourState = CreateTourState(),
    isCreate: Boolean = true,
    onNavigateBack: () -> Unit = {},
    onNameChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
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
            onBackClicked = onNavigateBack,
            modifier = Modifier.height(52.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.name),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )

                MainTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    value = uiState.name,
                    onValueChanged = onNameChanged,
                    labelText = stringResource(id = R.string.name),
                    placeholderText = stringResource(id = R.string.write_tour_name),
                    errorText = when (uiState.nameError) {
                        ValidationCases.EMPTY -> stringResource(id = R.string.tour_name_empty_error)
                        ValidationCases.ERROR -> stringResource(id = R.string.name_form_error)
                        else -> null
                    }
                )

                Text(
                    text = stringResource(id = R.string.description),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp)
                )

                MainTextField(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    value = uiState.description,
                    singleLine = false,
                    imeAction = ImeAction.Default,
                    onValueChanged = onDescriptionChanged,
                    labelText = stringResource(id = R.string.description),
                    placeholderText = stringResource(id = R.string.write_tour_description),
                    errorText = when (uiState.descriptionError) {
                        ValidationCases.EMPTY -> stringResource(id = R.string.description_empty_error)
                        ValidationCases.ERROR -> stringResource(id = R.string.description_form_error)
                        else -> null
                    }
                )
            }

            MainButton(
                text = stringResource(id = if (isCreate) R.string.create_tour else R.string.save),
                onClick = {
                    focusManager.clearFocus()
                    onSaveClicked()
                },
                isLoading = uiState.isLoading,
                isEnabled = !uiState.isLoading,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}