package com.castwave.castwave.ui.fragment.search.meditation

import android.os.Bundle
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Song
import com.castwave.castwave.databinding.FragmentChooseTimerBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseTimerFragment : BaseFragment<FragmentChooseTimerBinding>() {
    private var minute = Constants.INDEX_5
    private var second = Constants.INDEX_0
    private var dialogTimer: BottomSheetTimer? = null
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }

    private fun initView() {
        dialogTimer = BottomSheetTimer(Constants.INDEX_5) { newMinute, newSecond ->
            minute = newMinute
            second = newSecond
            setupTextTime(newMinute, newSecond)
        }
    }

    private fun getListSound(): ArrayList<Song> {
        return arrayListOf(
            Song(0, "Không"),
            Song(1, "Shurog"),
            Song(2, "Zhada"),
            Song(3, "Basu"),
            Song(4, "Kangse"),
            Song(5, "Ombu"),
            Song(6, "Dangze"),
            Song(7, "Dangze"),
            Song(8, "Tiếng nước chảy"),
            Song(9, "Tiếng thác đổ"),
            Song(10, "Tiếng mưa rơi"),
            Song(11, "Tiếng chim hót"),
            Song(12, "Tiếng lửa reo"),
            Song(13, "Thanh âm biển cả"),
            Song(14, "Thanh âm biển cả"),
            Song(15, "Thanh âm biển cả"),
            Song(16, "Thanh âm biển cả"),
            Song(17, "Thanh âm biển cả"),
            Song(18, "Thanh âm biển cả"),
            Song(19, "Thanh âm biển cả"),
            Song(20, "Thanh âm biển cả"),
        )
    }


    private fun setupTextTime(newMinute: Int, newSecond: Int) {
        binding.tvTotalTimer.text =
            String.format("$newMinute phút " + if (newSecond > Constants.INDEX_0) "$newSecond giây" else "")
    }

    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tbTime.setOnClickListener {
            dialogTimer?.show(childFragmentManager, ChooseTimerFragment::class.java.name)
        }
        binding.tvStartTimer.setOnClickListener {
            Bundle().apply {
                putLong(Constants.KEY_TIMER, (minute * 60 + second) * 1000.toLong())
                openScreenWithContainer(requireContext(), TimerFragment::class.java, this)
            }
        }
        binding.tbBackgroundSound.setOnClickListener {
            val  listItem =  getListSound()
           BottomSheetChooseSound("Âm thanh nền", listItem) { id ->
               binding.tvBackgroundSound.text = listItem[id].name
            }.show(childFragmentManager, ChooseTimerFragment::class.java.name)
        }
        binding.tbBellSound.setOnClickListener {
            val  listItem =  getListSound()
            BottomSheetChooseSound("Tiếng chuông", listItem) { id ->
                binding.tvBellSound.text = listItem[id].name
            }.show(childFragmentManager, ChooseTimerFragment::class.java.name)
        }
    }

    override fun onDestroyView() {
        dialogTimer = null
        super.onDestroyView()
    }
    override fun getLayoutBinding(): FragmentChooseTimerBinding =
        FragmentChooseTimerBinding.inflate(layoutInflater)
}