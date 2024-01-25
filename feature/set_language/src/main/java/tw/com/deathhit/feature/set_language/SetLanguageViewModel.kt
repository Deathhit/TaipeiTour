package tw.com.deathhit.feature.set_language

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import tw.com.deathhit.domain.LanguageRepository
import tw.com.deathhit.domain.enum_type.Language
import javax.inject.Inject

@HiltViewModel
class SetLanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var state: State
        get() = savedStateHandle[KEY_STATE] ?: State()
        set(value) {
            savedStateHandle[KEY_STATE] = value
        }
    val stateFlow = savedStateHandle.getStateFlow(KEY_STATE, state)

    val languageListFlow = createLanguageListFlow()

    fun onAction(action: State.Action) {
        state = state.copy(actions = state.actions - action)
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            languageRepository.setLanguage(language)

            state = state.copy(actions = state.actions + State.Action.Dismiss)
        }
    }

    private fun createLanguageListFlow() =
        languageRepository.getLanguageListFlow()

    companion object {
        private const val TAG = "SetLanguageViewModel"
        private const val KEY_STATE = "$TAG.KEY_STATE"
    }

    @Parcelize
    data class State(val actions: List<Action> = emptyList()) : Parcelable {
        sealed interface Action : Parcelable {
            @Parcelize
            data object Dismiss : Action
        }
    }
}