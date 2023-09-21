package com.tocaboca.tocacar.presentation.memory

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.tocaboca.tocacar.MainActivity
import com.tocaboca.tocacar.R
import com.tocaboca.tocacar.data.NeonCardDatabase
import com.tocaboca.tocacar.data.NeonCardRepository
import com.tocaboca.tocacar.databinding.FragmentMemoryBinding
import com.tocaboca.tocacar.databinding.LayoutGameRulesBinding
import com.tocaboca.tocacar.presentation.core.NeonCardViewModel
import com.tocaboca.tocacar.presentation.core.ClientWrapper
import com.tocaboca.tocacar.presentation.core.NeonCardEvent
import com.tocaboca.tocacar.presentation.core.NeonCardSettingsWrapper
import com.tocaboca.tocacar.presentation.core.ViewWrapper
import com.tocaboca.tocacar.presentation.core.additionalSetup
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MemoryFragment : Fragment() {

    private lateinit var viewBinding: FragmentMemoryBinding
    private val viewModel by viewModels<MemoryViewModel>()

    private val neonCardViewModel by viewModels<NeonCardViewModel> {
        object : ViewModelProvider.Factory {
            val database = NeonCardDatabase.getInstance(requireContext())

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST") return NeonCardViewModel(
                    neonCardRepository = NeonCardRepository(
                        database = database
                    )
                ) as T
            }
        }
    }

    private val memoryGame: MemoryGame = MemoryGame()

    private lateinit var adapt: BoardAdapter

    private var name: String? = null
    private var isAndroid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            name = it.getString(ARG_PARAM1)
            isAndroid = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMemoryBinding.inflate(inflater, container, false)

        return if (isAndroid == "0") {
            val view = (requireActivity() as MainActivity).NeonCardView(requireContext())

            val configView = ViewWrapper.getInstance(onMatch = {
                requireActivity().supportFragmentManager.commit {
                    replace(
                        R.id.fragment_container_view,
                        newInstance(param1 = "null", param2 = "1")
                    )
                    setReorderingAllowed(true)
                }
            }, onUnmatched = {
                neonCardViewModel.setEvent(event = NeonCardEvent.OnCardFlipped(name = it))
            })
            val client = ClientWrapper.getInstance(context = requireContext())

            viewBinding.root.visibility = View.GONE

            NeonCardSettingsWrapper().getInstance().configure(view)
                .additionalSetup(view = configView, client = client, name = name.toString())

        } else {
            setOrientation()
            hideSystemUI()

            viewBinding.gameRulesIcon.setOnClickListener {
                showAlertDialog(container)
            }

            adapt = BoardAdapter()
            adapt.onFlipped = {
                updateGameWithFlip(it)
            }

            adapt.memoryCards = memoryGame.memoryCards

            viewBinding.gameBoard.apply {
                this.adapter = adapt
                layoutManager = GridLayoutManager(requireContext(), 4)
                setHasFixedSize(true)
                addItemDecoration(BoardDecoration(4, 20, false))
            }

            viewBinding.root
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.isCardFacedUp(position)) {
            return
        }
        if (memoryGame.flipCard(position)) {
            if (memoryGame.wonGame()) {
                requireActivity().supportFragmentManager.commit {
                    add<WinFragment>(R.id.fragment_container_view)
                    setReorderingAllowed(true)
                }
            }
        }
        adapt.notifyDataSetChanged()
    }

    private fun setOrientation() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAndroid == "1") {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.secondsRemaining.collect {

                        val min = it / 60
                        val sec = it % 60

                        if (sec < 10) {
                            viewBinding.timerText.text = "$min:0$sec"
                        } else {
                            viewBinding.timerText.text = "$min:$sec"
                        }

                        if (it == 0L) {
                            requireActivity().supportFragmentManager.commit {
                                add<LoseFragment>(R.id.fragment_container_view)
                                setReorderingAllowed(true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showAlertDialog(root: ViewGroup?) {
        val dialog = Dialog(requireContext())

        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_game_rules, root)
        val binding = LayoutGameRulesBinding.bind(view)
        dialog.setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setLayout(1000, 800)

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        if (isAndroid == "1") {
            viewModel.startTimer()
            memoryGame.restore()
            adapt.memoryCards = memoryGame.memoryCards
            adapt.notifyDataSetChanged()
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.setDecorFitsSystemWindows(false)
            if (requireActivity().window.insetsController != null) {
                requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                requireActivity().window.insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            requireActivity().window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    companion object {

        private const val TAG = "MemoryFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) = MemoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

}




