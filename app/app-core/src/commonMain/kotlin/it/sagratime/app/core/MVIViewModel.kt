package it.sagratime.app.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MVIViewModel<State, Effect, Event> : ViewModel() {
    abstract val state: StateFlow<State>
    abstract val effects: SharedFlow<Effect>

    abstract fun onEvent(event: Event)
}

val EmptySharedFlow
    get() = MutableSharedFlow<Nothing>()
