package com.example.kotlinmvi.viewmodels

import android.util.Range
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.random
import kotlin.random.Random
import kotlin.random.nextInt

class MainViewModel : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    private var range:IntRange = IntRange(0,10)

    fun addRange(){
        range = IntRange(0, range.last + 1)
    }

    override fun createInitialState(): MainContract.State {
        return MainContract.State(MainContract.RandomNumberState.Idle)
    }

    override fun handleEvent(event: MainContract.Event) {

        when(event){
            is MainContract.Event.OnRandomNumberClicked -> {generateRandomNumber()}
            is MainContract.Event.OnShowToastClicked -> { setUiEffect{ MainContract.Effect.ShowToast }}
            is MainContract.Event.OnShowToastByClick -> { setUiEffect { MainContract.Effect.ShowToastByClick } }
            is MainContract.Event.OnAddRange -> {setUiEffect { MainContract.Effect.ShowAddRangeToast }}
        }
    }

    private fun generateRandomNumber(){

        viewModelScope.launch {
            setUiState { copy(randomNumberState = MainContract.RandomNumberState.Loading) }

            try {
                delay(2000)

                val randomNumber = Random.nextInt(range)

                if (randomNumber % 2 == 0){

                    setUiState { copy(randomNumberState = MainContract.RandomNumberState.Idle) }

                    throw RuntimeException("Number is even")
                }

                setUiState { copy(MainContract.RandomNumberState.Success(number = randomNumber)) }


            }
            catch (e:Exception){
                setUiEffect { MainContract.Effect.ShowToast }
            }
        }
    }
}