package com.baokiin.mymusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.data.model.PlayList
import com.baokiin.mymusic.databinding.ItemPublicPlayListBinding


class ItemPublicPlayListAdapter(private val onClick: (PlayList, Int) -> Unit) :
    ListAdapter<PlayList, ItemPublicPlayListAdapter.ViewHolder>(
        TitleDIff()
    ) {
    inner class ViewHolder(private val binding: ItemPublicPlayListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: PlayList, onClick: ((PlayList, Int) -> Unit)? = null) {
            binding.data = item

            itemView.apply {
                setOnClickListener {
                    if (onClick != null) {
                        onClick(item, adapterPosition)
                    }
                }
            }

            binding.executePendingBindings()

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPublicPlayListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClick) }
    }

    class TitleDIff : DiffUtil.ItemCallback<PlayList>() {
        // cung cấp thông tin về cách xác định phần
        override fun areItemsTheSame(
            oldItem: PlayList,
            newItem: PlayList
        ): Boolean { // cho máy biết 2 item_detail khi nào giống
            return oldItem.playlistId == newItem.playlistId // dung
        }

        override fun areContentsTheSame(
            oldItem: PlayList,
            newItem: PlayList
        ): Boolean { // cho biết item_detail khi nào cùng nội dung
            return oldItem == newItem
        }

    }
}

