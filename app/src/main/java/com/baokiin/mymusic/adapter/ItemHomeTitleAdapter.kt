package com.baokiin.mymusic.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.ItemTitleHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Integer.MAX_VALUE


class ItemHomeTitleAdapter(private val onClick: (Song) -> Unit) :
    ListAdapter<Song, ItemHomeTitleAdapter.ViewHolder>(
        TitleDIff()
    ) {
    class ViewHolder(private val binding: ItemTitleHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ItemTitleHomeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ViewHolder(
                    binding
                )
            }
        }

        fun bind(item: Song, onClick: ((Song) -> Unit)? = null) {
            binding.data = item
            itemView.setOnClickListener {
                if (onClick != null) {
                    onClick(item)
                }
            }

            binding.executePendingBindings()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, onClick) }
    }

}
