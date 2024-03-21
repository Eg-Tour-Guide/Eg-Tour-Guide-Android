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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.egtourguide.R
import com.egtourguide.auth.presentation.components.AuthHeader
import com.egtourguide.core.presentation.components.MainButton
import com.egtourguide.core.presentation.components.MainTextField
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme

@Preview(showBackground = true)
@Composable
private fun OtpScreenPreview() {
    EGTourGuideTheme {
        OtpScreen()
    }
}

@Composable
fun OtpScreen() {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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

        Text(
            text = stringResource(id = R.string.enter_verification_code_we_sent),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        var codeValue by remember {
            mutableStateOf("")
        }

        MainTextField(
            modifier = Modifier.fillMaxWidth(),
            value = codeValue,
            onValueChanged = {
                codeValue = it
            },
            labelText = stringResource(id = R.string.verification_code),
            placeholderText = stringResource(id = R.string.enter_verification_code),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

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
                Toast.makeText(context, "verified", Toast.LENGTH_SHORT).show()
            }
        )
    }
}