package com.egtourguide.home.presentation.screens.user

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.components.ScreenHeader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
) {
    Column {
        ScreenHeader(
            showSearch = true,
            showLogo = true,
            showCaptureObject = true,
            modifier = Modifier
                .height(52.dp)
        )
        Scaffold(
            bottomBar = {
                BottomBar(
                    selectedScreen = BottomBarScreens.User
                ) {

                }
            }) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .fillMaxSize()
                    .background(colorScheme.background)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.ic_user_placeholder),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(100.dp)
                                .width(100.dp)

                        )

                        Text(
                            text = (stringResource(id = R.string.user)),
                            style = MaterialTheme.typography.displayMedium,
                            color = colorScheme.primary
                        )

                    }
                }
                UserScreenItem(
                    iconId = R.drawable.ic_edit_profile,
                    textId = R.string.user,
                    onClick = {})
                UserScreenItem(iconId = R.drawable.ic_save, textId = R.string.saved, onClick = {})
                UserScreenItem(
                    iconId = R.drawable.ic_tours_unselected,
                    textId = R.string.my_tours,
                    onClick = {})
                UserScreenItem(
                    iconId = R.drawable.ic_settings,
                    textId = R.string.setting,
                    onClick = {})
                UserScreenItem(
                    iconId = R.drawable.ic_password,
                    textId = R.string.change_password,
                    onClick = {})
                UserScreenItem(
                    iconId = R.drawable.ic_logout,
                    tint = Color.Red,
                    textId = R.string.logout,
                    onClick = {})
            }

        }
    }

}


@Composable
private fun UserScreenItem(
    modifier: Modifier = Modifier,
    iconId: Int,
    textId: Int,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = textId),
            tint = tint
        )
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.displaySmall,
            color = colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() {
    EGTourGuideTheme {
        UserScreen()
    }
}