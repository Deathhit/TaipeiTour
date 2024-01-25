package tw.com.deathhit.feature.attraction_list

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.domain.AttractionRepository
import javax.inject.Inject

@HiltViewModel
class AttractionListViewModel @Inject constructor(
    private val attractionRepository: AttractionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var state: State
        get() = savedStateHandle[KEY_STATE] ?: State()
        set(value) {
            savedStateHandle[KEY_STATE] = value
        }
    val stateFlow = savedStateHandle.getStateFlow(KEY_STATE, state)

    val attractionPagingDataFlow = createAttractionPagingDataFlow().cachedIn(viewModelScope)

    fun goToAttractionDetailScreen(attractionId: String) {
        state =
            state.copy(
                actions = state.actions + State.Action.GoToAttractionDetailScreen(
                    attractionId = attractionId
                )
            )
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    fun setLanguage() {
        state = state.copy(actions = state.actions + State.Action.SetLanguage)
    }

    private fun createAttractionPagingDataFlow() =
        attractionRepository.getAttractionPagingDataFlow()

    companion object {
        private const val TAG = "AttractionListViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"
    }

    @Parcelize
    data class State(val actions: List<Action> = emptyList()) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data class GoToAttractionDetailScreen(val attractionId: String) : Action

            @Parcelize
            data object SetLanguage : Action
        }
    }
}