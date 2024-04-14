package com.egtourguide.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview(showBackground = true)
@Composable
private fun MainTextFieldPreview() {
    EGTourGuideTheme {
        var value by remember {
            mutableStateOf("")
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            MainTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(id = R.string.email),
                placeholderText = stringResource(id = R.string.enter_your_email),
                value = value,
                onValueChanged = {
                    value = it
                },
                imeAction = ImeAction.Done,
                errorText = stringResource(id = R.string.password_form_error)
            )
        }
    }
}

@Composable
fun MainTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChanged: (String) -> Unit = {},
    placeholderText: String = "",
    labelText: String = "",
    leadingIcon: Int? = null,
    leadingIconDescription: String? = null,
    trailingIcon: Int? = null,
    trailingIconDescription: String? = null,
    errorText: String? = null,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    var isShowPassword: Boolean by remember { mutableStateOf(false) }
    var visualTransformation: VisualTransformation by remember { mutableStateOf(VisualTransformation.None) }
    visualTransformation =
        if (isPassword && !isShowPassword) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChanged,
        shape = RoundedCornerShape(16.dp),
        enabled = isEnabled,
        isError = errorText != null,
        singleLine = singleLine,
        textStyle = MaterialTheme.typography.titleSmall,
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleSmall
            )
        },
        placeholder = {
            Text(
                text = placeholderText,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleSmall
            )
        },
        supportingText = if(errorText != null) {{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }} else null,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = MaterialTheme.colorScheme.outlineVariant,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            cursorColor = MaterialTheme.colorScheme.primary,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledTextColor = MaterialTheme.colorScheme.outline,
        ),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = leadingIconDescription,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else null,
        trailingIcon = if (trailingIcon != null) {
            {
                Icon(
                    painter = painterResource(id = trailingIcon),
                    contentDescription = trailingIconDescription,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else if (isPassword && value.isNotEmpty()) {
            {
                if (isShowPassword) {
                    visualTransformation = PasswordVisualTransformation()

                    IconButton(onClick = { isShowPassword = !isShowPassword }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_opened_eye),
                            contentDescription = stringResource(id = R.string.hide_password),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                } else {
                    visualTransformation = VisualTransformation.None

                    IconButton(onClick = { isShowPassword = !isShowPassword }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_closed_eye),
                            contentDescription = stringResource(id = R.string.show_password),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        } else null
    )
}