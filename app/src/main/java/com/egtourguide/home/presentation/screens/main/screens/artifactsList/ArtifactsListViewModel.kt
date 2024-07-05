package com.egtourguide.home.presentation.screens.main.screens.artifactsList

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.core.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import com.egtourguide.home.domain.usecases.GetArtifactsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class ArtifactsListViewModel @Inject constructor(
    private val getArtifactsListUseCase: GetArtifactsListUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase,
    private val detectArtifactUseCase: DetectArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtifactsListUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun onSaveClicked(artifact: AbstractedArtifact) {
        viewModelScope.launch(Dispatchers.IO) {
            artifact.isSaved = !artifact.isSaved
            changeArtifactSavedStateUseCase(artifactId = artifact.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = artifact.isSaved) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(saveError = error) }
                }
            )
        }
    }

    fun getArtifactsList() {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactsListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onSuccess = { response ->
                    val artifacts = filterArtifacts(artifacts = response, filters = filters)
                    _uiState.update { it.copy(isLoading = false, artifacts = artifacts) }
                }
            )
        }
    }

    private fun filterArtifacts(
        artifacts: List<AbstractedArtifact>,
        filters: HashMap<*, *>?
    ): List<AbstractedArtifact> {
        var resultedList = artifacts
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>

            when (filterKey) {
                "Artifact Type" -> {
                    if (filterValue.isNotEmpty()) {
                        resultedList = resultedList.filter { item ->
                            filterValue.contains(item.type)
                        }
                    }
                }

                "Location" -> {
                    if (filterValue.isNotEmpty()) {
                        resultedList = resultedList.filter { item ->
                            filterValue.contains(item.museumName)
                        }
                    }
                }

                "Material" -> {
                    if (filterValue.isNotEmpty()) {
                        resultedList = resultedList.filter { item ->
                            filterValue.contains(item.material)
                        }
                    }
                }
            }
        }
        return resultedList
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

    fun detectArtifact(image: Bitmap, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            detectArtifactUseCase(
                bitmap = image,
                context = context
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isDetectionLoading = true, detectedArtifact = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isDetectionLoading = false,
                            detectedArtifact = response,
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isDetectionLoading = false, error = error)
                    }
                }
            )
        }
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        val contentResolver: ContentResolver = context.contentResolver
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null

        try {
            inputStream = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return bitmap
    }

    fun clearDetectionSuccess() {
        _uiState.update { it.copy(detectedArtifact = null) }
    }
}