package com.expertsclub.expertspaging3.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.expertsclub.expertspaging3.R
import com.expertsclub.expertspaging3.databinding.ItemCharacterLoadStateBinding

class CharactersLoadStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_character_load_state, parent, false)
) {
    private val binding = ItemCharacterLoadStateBinding.bind(itemView)
    private val progressBar: ProgressBar = binding.progressBar
    private val textErrorMessage: TextView = binding.textErrorMessage
    private val buttonRetry: Button = binding.buttonRetry.also {
        it.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        buttonRetry.isVisible = loadState is LoadState.Error
        textErrorMessage.isVisible = loadState is LoadState.Error
    }
}