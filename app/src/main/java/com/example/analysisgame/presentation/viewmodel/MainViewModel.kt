package com.example.analysisgame.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.analysisgame.domain.model.AnswerRequest
import com.example.analysisgame.domain.model.AnswerResponse
import com.example.analysisgame.domain.model.Resource
import com.example.analysisgame.domain.model.UserRequest
import com.example.analysisgame.domain.model.UserResponse
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

    private val _userState = MutableStateFlow<Resource<UserResponse>>(Resource.Initial)
    val userState: StateFlow<Resource<UserResponse>> = _userState.asStateFlow()

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            _userState.value = Resource.Loading
            try {
                val response = repository.registerUser(userRequest)
                _userState.value = Resource.Success(response)
            } catch (e: Exception) {
                _userState.value = Resource.Error(e)
            }
        }
    }

    fun successUserState(username: String){
        _userState.value = Resource.Success(UserResponse(1, username, "", null))
    }


    private val _answerState = MutableStateFlow<Resource<AnswerResponse>>(Resource.Initial)
    val answerResponse: StateFlow<Resource<AnswerResponse>> = _answerState.asStateFlow()

    fun createAnswer(answerRequest: AnswerRequest){
        viewModelScope.launch {
            _answerState.value = Resource.Loading
            try {
                val response = repository.createAnswer(answerRequest)
                _answerState.value = Resource.Success(response)
            } catch (e: Exception) {
                _answerState.value = Resource.Error(e)
            }
        }
    }

    private val _profileState = MutableStateFlow<Resource<UserResponse>>(Resource.Initial)
    val profileState: StateFlow<Resource<UserResponse>> = _profileState.asStateFlow()

    fun getUserResult(username: String){
        viewModelScope.launch {
            _profileState.value = Resource.Loading
            try {
                val response = repository.getUserByUsername(username)
                _profileState.value = Resource.Success(response)
            } catch (e: Exception) {
                _profileState.value = Resource.Error(e)
            }
        }
    }
}