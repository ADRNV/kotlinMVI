package com.example.kotlinmvi.viewmodels

import com.example.kotlinmvi.presentation.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

abstract class BaseViewModel<Event : UiEvent, State : UiState, Effect : UiEffect> : ViewModel() {

    private val initialState : State by lazy { createInitialState() }

    private val currentState: State//Состояние UI
        get() = uiState.value

    abstract fun createInitialState() : State

    private val _uiState:MutableStateFlow<State> = MutableStateFlow<State>(initialState)
    val  uiState
    get() = _uiState.asStateFlow()


    private val _event:MutableSharedFlow<Event> = MutableSharedFlow<Event>()
    private val event
    get() = _event.asSharedFlow()


    private val _uiEffect:Channel<Effect> = Channel()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {

        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }

    }

    abstract fun handleEvent(event:Event)

    fun setEvent(Event:Event){

        viewModelScope.launch { _event.emit(Event) }
    }

    protected fun setUiEffect(builder:() -> Effect){

        viewModelScope.launch { _uiEffect.send(builder.invoke()) }

    }

    protected fun setUiState(reduce:State.() -> State){

        _uiState.value = currentState.reduce()

    }

}