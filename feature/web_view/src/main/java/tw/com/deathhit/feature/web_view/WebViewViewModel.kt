package tw.com.deathhit.feature.web_view

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class WebViewViewModel @Inject constructor(
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

    fun goBack() {
        state = state.copy(actions = state.actions + State.Action.GoBack)
    }

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    companion object {
        private const val TAG = "WebViewViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"

        internal fun createArgs(startUrl: String, title: String) = Bundle().apply {
            putParcelable(
                KEY_STATE, State(
                    startUrl = startUrl,
                    title = title
                )
            )
        }
    }

    @Parcelize
    data class State(
        val actions: List<Action> = emptyList(),
        val startUrl: String,
        val title: String
    ) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data object GoBack : Action
        }
    }
}