package com.expertsclub.expertspaging3.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.expertsclub.expertspaging3.R
import com.expertsclub.expertspaging3.data.model.Character
import com.expertsclub.expertspaging3.data.network.RetrofitService
import com.expertsclub.expertspaging3.data.network.RickMortyApi
import com.expertsclub.expertspaging3.data.repository.CharactersRepositoryImpl
import com.expertsclub.expertspaging3.databinding.CharactersFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class CharactersFragment : Fragment() {

    private var _binding: CharactersFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModelFactory = CharactersViewModel.Factory(
        CharactersRepositoryImpl(RetrofitService.rickMortyApi)
    )

    private lateinit var viewModel: CharactersViewModel

    private val charactersAdapter = CharactersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CharactersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeInitialLoadState()
        setRecyclerCharacters()
        loadCharacters()
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                binding.flipperCharacters.displayedChild = when (loadState.refresh) {
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        0
                    }
                    is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        1
                    }
                    is LoadState.Error -> {
                        setShimmerVisibility(false)
                        binding.buttonRetry.setOnClickListener {
                            charactersAdapter.refresh()
                        }
                        2
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.shimmerCharacters.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    private fun setRecyclerCharacters() {
        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateFooter(
                CharacterLoadStateAdapter(charactersAdapter::retry)
            )
        }
    }

    private fun loadCharacters() {
        lifecycleScope.launch {
            viewModel.charactersFlow.collectLatest { pagingData ->
                charactersAdapter.submitData(pagingData)
            }
        }
    }
}