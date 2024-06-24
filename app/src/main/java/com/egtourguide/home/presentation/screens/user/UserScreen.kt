package com.egtourguide.home.presentation.screens.user

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
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


}

@Composable
fun UserScreenContent(
    modifier: Modifier = Modifier
) {

}

@Composable
fun UserDataSection(
    userPhoto: String,
    userName: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MainImage(
            modifier = Modifier.clip(CircleShape),
            data = "/userImages/$userPhoto"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userName,
            style = MaterialTheme.typography.displaySmall,
            color = colorScheme.onBackground
        )
    }
}

@Composable
fun UserScreenOptions(
    onSavedClicked: () -> Unit,
    onMyToursClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UserScreenOption(
            optionName = "Saved",
            optionIcon = R.drawable.ic_save,
            onOptionClick = onSavedClicked
        )
        UserScreenOption(
            optionName = "My Tours",
            optionIcon = R.drawable.ic_tours_unselected,
            onOptionClick = onMyToursClicked
        )
    }
}


@Composable
private fun UserScreenOption(
    optionName: String,
    optionIcon: Int,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onOptionClick()
        },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            painter = painterResource(id = optionIcon),
            contentDescription = optionName,
            tint = Color.Unspecified
        )
        Text(
            text = optionName,
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