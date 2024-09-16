package tw.com.deathhit.feature.event_list

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.domain.EventRepository
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(savedStateHandle[KEY_STATE] ?: State())
    val stateFlow = _stateFlow.asStateFlow()

    private var state: State
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }

            savedStateHandle[KEY_STATE] = value
        }

    val eventPagingDataFlow = createEventPagingDataFlow().cachedIn(viewModelScope)

    fun goToEventWebsite(startUrl: String, title: String) {
        state =
            state.copy(
                actions = state.actions + State.Action.GoToEventWebsite(
                    startUrl = startUrl,
                    title = title
                )
            )
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    private fun createEventPagingDataFlow() =
        eventRepository.getEventPagingDataFlow()

    companion object {
        private const val TAG = "EventListViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"
    }

    @Parcelize
    data class State(val actions: List<Action> = emptyList()) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data class GoToEventWebsite(val startUrl: String, val title: String) : Action
        }
    }
}