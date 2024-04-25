package com.egtourguide.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.egtourguide.R
import kotlinx.coroutines.Dispatchers

// TODO: Change default images!!
@Composable
fun MainImage(
    data: Any,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    @DrawableRes placeHolderImage: Int = R.drawable.ic_launcher_background,
    @DrawableRes errorImage: Int = R.drawable.ic_error
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data)
                .crossfade(enable = true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .dispatcher(Dispatchers.IO)
                .placeholder(placeHolderImage)
                .error(errorImage)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}