package it.sagratime.app.core.feature.cards.welcome

import androidx.lifecycle.viewModelScope
import it.sagratime.app.core.MVIViewModel
import it.sagratime.app.core.repository.V1EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeCardViewModel(
    private val eventRepository: V1EventRepository,
) : MVIViewModel<WelcomeCardState, Nothing, Nothing>() {
    private val _state =
        MutableStateFlow<WelcomeCardState>(WelcomeCardState.Loading)

    override val state: StateFlow<WelcomeCardState> =
        _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = WelcomeCardState.Ready(eventRepository.getEventStatistics())
        }
    }

    override fun onEvent(event: Nothing) {}
}
