package com.baokiin.mymusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.ItemHomeBinding


class ItemHomeAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<Song, ItemHomeAdapter.ViewHolder>(
        TitleDIff()
    ) {
    class ViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ItemHomeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                return ViewHolder(
                    binding
                )
            }
        }

        fun bind(item: Song, onClick: ((Int) -> Unit)? = null) {
            binding.data = item
            itemView.setOnClickListener {
                if (onClick != null) {

                    onClick(adapterPosition)
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
        getItem(position)?.let { holder.bind(it, onClick) }
    }
}
class TitleDIff : DiffUtil.ItemCallback<Song>() {
    // cung cấp thông tin về cách xác định phần
    override fun areItemsTheSame(
        oldItem: Song,
        newItem: Song
    ): Boolean { // cho máy biết 2 item_detail khi nào giống
        return oldItem.songId == newItem.songId // dung
    }

    override fun areContentsTheSame(
        oldItem: Song,
        newItem: Song
    ): Boolean { // cho biết item_detail khi nào cùng nội dung
        return oldItem == newItem
    }

}

