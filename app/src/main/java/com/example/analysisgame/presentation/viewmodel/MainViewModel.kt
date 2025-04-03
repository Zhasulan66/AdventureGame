package com.example.analysisgame.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.analysisgame.domain.model.LoremText
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.domain.repository.AnalysisGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AnalysisGameRepository,
    private val application: Application
) : ViewModel() {

    private val _loremState = MutableStateFlow<Resource<LoremText>>(Resource.Initial)
    val loremState: StateFlow<Resource<LoremText>> = _loremState.asStateFlow()

    fun fetchLoremWithNum(num: String) {
        viewModelScope.launch {
            _loremState.value = Resource.Loading
            try {
                val response = repository.getLoremText(num)
                _loremState.value = Resource.Success(response)
            } catch (e: Exception) {
                _loremState.value = Resource.Error(e)
            }
        }
    }
}