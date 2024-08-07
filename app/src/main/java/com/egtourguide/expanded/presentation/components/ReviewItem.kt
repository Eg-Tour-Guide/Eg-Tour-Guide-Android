package com.egtourguide.expanded.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egtourguide.R
import com.egtourguide.core.presentation.components.MainImage
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.getLoremString
import com.egtourguide.home.domain.model.Review

@Preview(showBackground = true)
@Composable
private fun ReviewItemPreview() {
    EGTourGuideTheme {
        ReviewItem(
            review = Review(
                id = "",
                authorName = "Abdo Sharaf",
                authorImage = "",
                rating = 5,
                description = getLoremString(words = 20)
            )
        )
    }
}

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainImage(
                data = review.authorImage,
                errorImage = R.drawable.ic_profile_pic,
                placeholderImage = R.drawable.ic_profile_pic,
                contentDescription = stringResource(id = R.string.profile_picture),
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )

            Text(
                text = review.authorName,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        RatingBar(
            rating = review.rating,
            starSize = 20.dp,
            starPadding = 6.dp,
            isClickable = false,
            onRatingChanged = {},
            modifier = Modifier.padding(top = 12.dp)
        )

        SelectionContainer(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = review.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}