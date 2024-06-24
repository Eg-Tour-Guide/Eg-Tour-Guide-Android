package com.egtourguide.home.presentation.screens.user

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.egtourguide.R
import com.egtourguide.home.presentation.components.BottomBar
import com.egtourguide.home.presentation.components.BottomBarScreens
import com.egtourguide.home.presentation.components.ScreenHeader

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
) {
    Column {
        ScreenHeader(
            showSearch = true,
            showLogo = true,
            showCaptureObject = true
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .clip(CircleShape)
                    ){
                        AsyncImage(
                            model = painterResource(id = R.drawable.ic_user_placeholder),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Text(
                        text = (stringResource(id = R.string.user)),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
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
            UserScreenItem(iconId = R.drawable.ic_settings, textId = R.string.setting, onClick = {})
            UserScreenItem(
                iconId = R.drawable.ic_password,
                textId = R.string.change_password,
                onClick = {})
            UserScreenItem(iconId = R.drawable.ic_logout, textId = R.string.logout, onClick = {})
        }
//        BottomBar(selectedScreen =BottomBarScreens.User , modifier = Modifier.align(Alignment.End)) {

//        }
    }
}


@Composable
private fun UserScreenItem(
    modifier: Modifier = Modifier,
    iconId: Int,
    textId: Int,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = textId)
        )
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() {
    UserScreen()
}