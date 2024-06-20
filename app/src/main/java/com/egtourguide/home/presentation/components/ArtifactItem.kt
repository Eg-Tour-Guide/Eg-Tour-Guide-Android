package com.egtourguide.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.ARTIFACT_IMAGE_LINK_PREFIX
import com.egtourguide.home.domain.model.AbstractedArtifact

@Composable
fun ArtifactItem(
    artifact: AbstractedArtifact,
    onArtifactClicked: (AbstractedArtifact) -> Unit,
    onSaveClicked: (AbstractedArtifact) -> Unit
) {
    Column(
        modifier = Modifier
            .width(144.dp)
            .height(190.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onArtifactClicked(artifact)
            }
    ) {
        MainImage(
            modifier = Modifier
                .height(105.dp)
                .clip(RoundedCornerShape(4.dp)),
            data = "$ARTIFACT_IMAGE_LINK_PREFIX${artifact.image}",
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.height(36.dp),
            text = artifact.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_location),
                    tint = Color.Unspecified,
                    contentDescription = "Location Icon"
                )
                Text(
                    text = " ${artifact.museumName}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    onSaveClicked(artifact)
                }
            ) {
                Icon(
                    painter = painterResource(id = if (artifact.isSaved) R.drawable.ic_saved else R.drawable.ic_save),
                    contentDescription = stringResource(id = if (artifact.isSaved) R.string.unsave else R.string.save),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
private fun ArtifactItemPreview() {
    EGTourGuideTheme {
        ArtifactItem(
            artifact = AbstractedArtifact(
                id = "",
                name = "Pyramids",
                image = "https://www.worldhistory.org/uploads/images/5687.jpg",
                museumName = "Giza",
                isSaved = false,
            ),
            onArtifactClicked = {},
            onSaveClicked = {},
        )
    }
}