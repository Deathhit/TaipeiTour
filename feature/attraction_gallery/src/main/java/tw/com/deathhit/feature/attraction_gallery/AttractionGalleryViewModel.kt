package tw.com.deathhit.feature.attraction_gallery

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.domain.AttractionImageRepository
import tw.com.deathhit.domain.AttractionRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AttractionGalleryViewModel @Inject constructor(
    private val attractionImageRepository: AttractionImageRepository,
    private val attractionRepository: AttractionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var state: State
        get() = savedStateHandle[KEY_STATE]!!
        set(value) {
            savedStateHandle[KEY_STATE] = value
        }
    val stateFlow = savedStateHandle.getStateFlow(KEY_STATE, state)

    val attractionImageListFlow = createAttractionListFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val attractionFlow =
        createAttractionFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun goBack() {
        state = state.copy(actions = state.actions + State.Action.GoBack)
    }

    fun goToImageViewerScreen(imageUrl: String) {
        state =
            state.copy(
                actions = state.actions + State.Action.GoToImageViewerScreen(
                    imageUrl = imageUrl
                )
            )
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    private fun createAttractionFlow() = stateFlow.map { it.attractionId }.flatMapLatest {
        attractionRepository.getAttractionFlow(it)
    }

    private fun createAttractionListFlow() = stateFlow.map { it.attractionId }.flatMapLatest {
        attractionImageRepository.getAttractionImageListFlow(attractionId = it)
    }

    companion object {
        private const val TAG = "AttractionGalleryViewModel"
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
    data class State(val actions: List<Action> = emptyList(), val attractionId: String) :
        Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data object GoBack : Action

            @Parcelize
            data class GoToImageViewerScreen(val imageUrl: String) : Action
        }
    }
}