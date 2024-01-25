package tw.com.deathhit.feature.image_viewer

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private var state: State
        get() = savedStateHandle[KEY_STATE]!!
        set(value) {
            savedStateHandle[KEY_STATE] = value
        }
    val stateFlow = savedStateHandle.getStateFlow(KEY_STATE, state)

    fun goBack() {
        state = state.copy(actions = state.actions + State.Action.GoBack)
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    companion object {
        private const val TAG = "ImageViewerViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"

        internal fun createArgs(imageUrl: String) = Bundle().apply {
            putParcelable(
                KEY_STATE, State(
                    imageUrl = imageUrl
                )
            )
        }
    }

    @Parcelize
    data class State(val actions: List<Action> = emptyList(), val imageUrl: String) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data object GoBack : Action
        }
    }
}