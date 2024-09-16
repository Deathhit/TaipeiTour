package tw.com.deathhit.feature.attraction_detail

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.domain.AttractionRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AttractionDetailViewModel @Inject constructor(
    private val attractionRepository: AttractionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<State>(savedStateHandle[KEY_STATE]!!)
    val stateFlow = _stateFlow.asStateFlow()

    private var state: State
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }

            savedStateHandle[KEY_STATE] = value
        }

    val attractionFlow =
        createAttractionFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun goBack() {
        state = state.copy(actions = state.actions + State.Action.GoBack)
    }

    fun goToGalleryScreen() {
        viewModelScope.launch {
            val attraction = createAttractionFlow().first() ?: return@launch

            state = state.copy(
                actions = state.actions + State.Action.GoToGalleryScreen(
                    attractionId = attraction.attractionId
                )
            )
        }
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    private fun createAttractionFlow() = stateFlow.map { it.attractionId }.flatMapLatest {
        attractionRepository.getAttractionFlow(it)
    }

    companion object {
        private const val TAG = "AttractionDetailViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"

        internal fun createArgs(attractionId: String) = Bundle().apply {
            putParcelable(
                KEY_STATE, State(
                    attractionId = attractionId
                )
            )
        }
    }

    @Parcelize
    data class State(
        val actions: List<Action> = emptyList(),
        val attractionId: String
    ) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data object GoBack : Action

            @Parcelize
            data class GoToGalleryScreen(val attractionId: String) : Action
        }
    }
}