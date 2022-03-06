package com.example.kotlinmvi.viewmodels

import com.example.kotlinmvi.presentation.UiEffect
import com.example.kotlinmvi.presentation.UiEvent
import com.example.kotlinmvi.presentation.UiState

class MainContract {


    sealed class Event : UiEvent {

        object OnRandomNumberClicked : Event()
        object OnShowToastClicked : Event()
        object OnStartNewActivity : Event()
        object OnShowToastByClick : Event()
        object OnAddRange : Event()
    }


    data class State(

        val randomNumberState: RandomNumberState,
        val isLoading:Boolean = false,
        val randomNumber:Int = -1,
        val error:String? = null

    ) : UiState


    sealed class RandomNumberState {
        object Idle : RandomNumberState()
        object Loading : RandomNumberState()
        data class Success(val number : Int) : RandomNumberState()
    }


    sealed class Effect : UiEffect {

        object ShowToast : Effect()

        object ShowNewActivity: Effect()

        object ShowToastByClick : Effect()

        object ShowAddRangeToast : Effect()

    }

}