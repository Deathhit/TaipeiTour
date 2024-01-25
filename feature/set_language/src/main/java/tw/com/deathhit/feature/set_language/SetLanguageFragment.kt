package tw.com.deathhit.feature.set_language

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.set_language.databinding.FragmentSetLanguageBinding

@AndroidEntryPoint
class SetLanguageFragment : BottomSheetDialogFragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentSetLanguageBinding? = null

    private val viewModel: SetLanguageViewModel by viewModels()

    private val languageListAdapter get() = _languageListAdapter!!
    private var _languageListAdapter: LanguageListAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentSetLanguageBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            adapter = createLanguageListAdapter().also { _languageListAdapter = it }
            setHasFixedSize(true)
        }

        bindViewModelState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null

        _binding = null
        _languageListAdapter = null
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.distinctUntilChanged()
                        .collectLatest { actions ->
                            actions.forEach { action ->
                                when (action) {
                                    SetLanguageViewModel.State.Action.Dismiss -> dismiss()
                                }

                                viewModel.onAction(action)
                            }
                        }
                }

                launch {
                    viewModel.languageListFlow.collectLatest {
                        languageListAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun createLanguageListAdapter() = LanguageListAdapter {
        viewModel.setLanguage(it.language)
    }.also { _languageListAdapter = it }

    companion object {
        fun create() = SetLanguageFragment()
    }
}