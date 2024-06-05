package com.egtourguide.home.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.egtourguide.home.domain.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    fun onSaveClicked(place: Place) {

    }

}