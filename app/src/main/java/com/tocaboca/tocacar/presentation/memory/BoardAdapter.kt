package com.tocaboca.tocacar.presentation.memory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tocaboca.tocacar.R
import com.tocaboca.tocacar.databinding.MemoryCardLayoutBinding

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    var memoryCards = emptyList<MemoryCard>()
    lateinit var onFlipped: (Int) -> Unit

    inner class BoardViewHolder(
        private val binding: MemoryCardLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(memoryCard: MemoryCard, pos: Int) {
            with(binding) {
                imageButton.setImageResource(if (memoryCard.facedUp) memoryCard.id else R.drawable.blank_card)

                imageButton.alpha = if (memoryCard.matched) .4f else 1.0f

                imageButton.setOnClickListener {
                    onFlipped(pos)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding =
            MemoryCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BoardViewHolder(binding)
    }

    override fun getItemCount(): Int = memoryCards.size

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(memoryCards[position], position)
    }

}