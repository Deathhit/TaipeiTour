package tw.com.deathhit.feature.event_list

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.domain.EventRepository
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var state: State
        get() = savedStateHandle[KEY_STATE] ?: State()
        set(value) {
            savedStateHandle[KEY_STATE] = value
        }
    val stateFlow = savedStateHandle.getStateFlow(KEY_STATE, state)

    val eventPagingDataFlow = createEventPagingDataFlow().cachedIn(viewModelScope)

    fun goToEventDetailScreen(eventId: String) {
        state =
            state.copy(
                actions = state.actions + State.Action.GoToEventDetailScreen(
                    eventId = eventId
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
            data class GoToEventDetailScreen(val eventId: String) : Action
        }
    }
}