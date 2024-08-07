package com.egtourguide.core.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapItem(
    title: String,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    // TODO: Use remember to disable flashes!!
    val context = LocalContext.current
    val place = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(place, 10f)
    }

    val openMapsApp = {
        val uri = Uri.parse("geo:0,0?q=${place.latitude},${place.longitude}($title)")
        Intent(Intent.ACTION_VIEW, uri).also { intent ->
            intent.setPackage("com.google.android.apps.maps")
            context.startActivity(intent)
        }
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer)
    )

    /*GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            myLocationButtonEnabled = false
        ),
        onMapClick = {
            openMapsApp()
        }
    ) {
        Marker(
            state = MarkerState(position = place),
            onClick = {
                openMapsApp()
                false
            }
        )
    }*/
}