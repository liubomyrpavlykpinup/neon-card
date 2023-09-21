package com.tocaboca.tocacar.presentation.load

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tocaboca.tocacar.R
import com.tocaboca.tocacar.data.NeonCardDatabase
import com.tocaboca.tocacar.data.NeonCardRepository
import com.tocaboca.tocacar.openMemoryGameFragment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadFragment : Fragment(R.layout.fragment_load) {

    private val viewModel by viewModels<LoadViewModel> {
        object : ViewModelProvider.Factory {
            val database = NeonCardDatabase.getInstance(requireContext())

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoadViewModel(neonCardRepository = NeonCardRepository(database = database)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: LoadFragment")

        viewModel.setEvent(event = LoadEvent.Load(requireActivity()))
        observeState(state = viewModel.configurationState)
    }

    private fun observeState(state: StateFlow<LoadState>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                state.collect {
                    when (it) {
                        LoadState.Loading -> {
                        }

                        is LoadState.Loaded -> {
                            requireActivity().openMemoryGameFragment(
                                name = it.name,
                                isAndroid = "0"
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoadFragment"
    }
}