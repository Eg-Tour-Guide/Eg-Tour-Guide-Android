package com.egtourguide.home.presentation.screens.main.screens.toursList

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import com.egtourguide.home.domain.usecases.GetToursListUseCase
import com.egtourguide.home.presentation.screens.filter.FilterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ToursListViewModel @Inject constructor(
    private val getToursListUseCase: GetToursListUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
    private val detectArtifactUseCase: DetectArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToursListUIState())
    val uiState = _uiState.asStateFlow()

    fun getToursList() {
        viewModelScope.launch(Dispatchers.IO) {
            getToursListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tours = response,
                            displayedTours = response
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                }
            )
        }
    }

    fun onSaveClicked(tour: AbstractedTour) {
        viewModelScope.launch(Dispatchers.IO) {
            changeTourSavedStateUseCase(tourId = tour.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = !tour.isSaved) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(saveError = error) }
                }
            )
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

    fun filterTours(filterState: FilterScreenState) {
        /*var resultedList = tours
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>
            when (filterKey) {
                "Rating" -> {
                    if (filterValue.isNotEmpty()) {
                        val ratingValue = filterValue.first().toInt()
                        resultedList = resultedList.filter { item ->
                            item.rating >= ratingValue
                        }
                    }
                }

                "Duration" -> {
                    if (filterValue.size == 2) {
                        val minDays = filterValue.first().toInt()
                        val maxDays = filterValue.last().toInt()
                        resultedList = resultedList.filter { item ->
                            item.duration in minDays..maxDays
                        }
                    }
                }

                "Sort By" -> {
                    if (filterValue.isNotEmpty()) {
                        val sortType = filterValue.first().toInt()
                        resultedList = if (sortType == 0) {
                            resultedList.sortedBy { item -> item.rating }
                        } else {
                            resultedList.sortedByDescending { item -> item.rating }
                        }
                    }
                }
            }
        }
        return resultedList*/

        _uiState.update {
            it.copy(displayedTours = it.displayedTours.filter { tour ->
                filterState.minDuration.toInt() <= tour.duration && tour.duration <= filterState.maxDuration.toInt()
            })
        }
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