package tw.com.deathhit.feature.set_day_night

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import tw.com.deathhit.feature.set_day_night.databinding.FragmentSetDayNightBinding
import tw.com.deathhit.feature.set_day_night.enum_type.DayNightMode
import tw.com.deathhit.feature.set_day_night.model.DayNightItem

@AndroidEntryPoint
class SetDayNightFragment : BottomSheetDialogFragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentSetDayNightBinding? = null

    private val dayNightListAdapter get() = _dayNightListAdapter!!
    private var _dayNightListAdapter: DayNightListAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentSetDayNightBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            adapter = createDayNightListAdapter().also { _dayNightListAdapter = it }
        }

        updateModeList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null

        _binding = null
        _dayNightListAdapter = null
    }

    private fun createDayNightListAdapter() = DayNightListAdapter {
        AppCompatDelegate.setDefaultNightMode(
            when (it.mode) {
                DayNightMode.DAY -> AppCompatDelegate.MODE_NIGHT_NO
                DayNightMode.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
                DayNightMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )

        updateModeList()

        dismiss()
    }.also { _dayNightListAdapter = it }

    private fun updateModeList() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()

        dayNightListAdapter.submitList(modeList.map {
            DayNightItem(isSelected = when(it) {
                DayNightMode.DAY -> currentMode == AppCompatDelegate.MODE_NIGHT_NO
                DayNightMode.NIGHT -> currentMode == AppCompatDelegate.MODE_NIGHT_YES
                DayNightMode.SYSTEM -> currentMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }, mode = it)
        })
    }

    companion object {
        private val modeList = listOf(DayNightMode.DAY, DayNightMode.NIGHT, DayNightMode.SYSTEM)

        fun create() = SetDayNightFragment()
    }
}