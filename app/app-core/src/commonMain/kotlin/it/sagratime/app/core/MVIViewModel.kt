package it.sagratime.app.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class MVIViewModel<State, Effect, Event> : ViewModel() {
    abstract val state: StateFlow<State>

    @Suppress("PropertyName")
    protected val _effects: MutableSharedFlow<Effect> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    abstract fun onEvent(event: Event)
}
