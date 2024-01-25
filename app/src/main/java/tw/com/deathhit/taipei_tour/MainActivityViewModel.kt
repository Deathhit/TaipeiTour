package tw.com.deathhit.taipei_tour

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.taipei_tour.model.MainScreen
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private var state: State
        get() = savedStateHandle[KEY_STATE] ?: State()
        set(value) {
            savedStateHandle[KEY_STATE] = value
        }
    val stateFlow = savedStateHandle.getStateFlow(KEY_STATE, state)

    fun goBack() {
        state = state.copy(actions = state.actions + State.Action.GoBack)
    }

    fun goToAttractionDetailsScreen(attractionId: String) {
        state = state.copy(
            actions = state.actions + State.Action.GoToScreen(
                screen = MainScreen.AttractionDetail(
                    attractionId = attractionId
                )
            )
        )
    }

    fun goToGalleryScreen(attractionId: String) {
        state = state.copy(
            actions = state.actions + State.Action.GoToScreen(
                screen = MainScreen.Gallery(
                    attractionId = attractionId
                )
            )
        )
    }

    fun goToImageViewer(imageUrl: String) {
        state = state.copy(
            actions = state.actions + State.Action.GoToScreen(
                MainScreen.ImageViewer(imageUrl)
            )
        )
    }

    fun goToInitialScreen() {
        state = state.copy(
            actions = state.actions + State.Action.GoToInitialScreen(screen = MainScreen.AttractionList)
        )
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    companion object {
        private const val TAG = "MainActivityViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"
    }

    @Parcelize
    data class State(val actions: List<Action> = emptyList()) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data object GoBack : Action

            @Parcelize
            data class GoToInitialScreen(val screen: MainScreen) : Action

            @Parcelize
            data class GoToScreen(val screen: MainScreen) : Action
        }
    }
}