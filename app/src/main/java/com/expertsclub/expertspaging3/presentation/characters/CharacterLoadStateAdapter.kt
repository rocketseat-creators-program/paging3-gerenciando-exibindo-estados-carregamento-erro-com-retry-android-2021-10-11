package com.expertsclub.expertspaging3.presentation.characters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CharacterLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharactersLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = CharactersLoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: CharactersLoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)
}