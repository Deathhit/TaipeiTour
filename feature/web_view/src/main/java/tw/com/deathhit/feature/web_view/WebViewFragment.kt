package tw.com.deathhit.feature.web_view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tw.com.deathhit.feature.web_view.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    var callback: Callback? = null

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WebViewViewModel by viewModels()

    private val onGoBackListener = View.OnClickListener {
        viewModel.goBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentWebViewBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureWebView()

        bindViewModelState()
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            toolbar.setNavigationOnClickListener(onGoBackListener)
            webView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            toolbar.setNavigationOnClickListener(null)
            webView.onPause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(binding.webView) {
            requireArguments().webViewState = Bundle().also { saveState(it) }

            destroy()
        }

        _binding = null
    }

    fun goBackInWebView() {
        binding.webView.goBack()
    }

    private fun bindViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.map { it.actions }.collectLatest { actions ->
                        actions.forEach { action ->
                            when (action) {
                                WebViewViewModel.State.Action.GoBack -> callback?.onGoBack(
                                    isWebViewCanGoBack = binding.webView.canGoBack()
                                )
                            }

                            viewModel.onAction(action)
                        }
                    }
                }

                launch {
                    viewModel.stateFlow.map { it.title }.distinctUntilChanged().collectLatest {
                        binding.toolbar.title = it
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        with(binding.webView) {
            with(settings) {
                javaScriptEnabled = true
            }

            url ?: run {
                loadUrl(viewModel.stateFlow.value.startUrl)
            }

            requireArguments().webViewState?.let { restoreState(it) }
        }
    }

    companion object {
        private const val TAG = "WebViewFragment"
        private const val KEY_WEB_VIEW_STATE = "$TAG.KEY_WEB_VIEW_STATE"

        private var Bundle.webViewState
            get() = getBundle(KEY_WEB_VIEW_STATE)
            set(value) {
                putBundle(KEY_WEB_VIEW_STATE, value)
            }
    }

    interface Callback {
        fun onGoBack(isWebViewCanGoBack: Boolean)
    }
}