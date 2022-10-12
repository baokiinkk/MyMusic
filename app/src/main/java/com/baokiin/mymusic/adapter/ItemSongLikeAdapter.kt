package com.baokiin.mymusic.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.SongLike
import com.baokiin.mymusic.databinding.ItemSongLikedListBinding
import kotlinx.android.synthetic.main.item_play_list.view.*


class ItemSongLikeAdapter(private val onClick: (Song, Int) -> Unit) :
    ListAdapter<Song, ItemSongLikeAdapter.ViewHolder>(
        SongLikeDIff()
    ) {
    var tmpItem: Song? = null
    inner class ViewHolder(private val binding: ItemSongLikedListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Song, onClick: ((Song,Int) -> Unit)? = null) {
            binding.data = item

            itemView.apply {
                setBackgroundColor(
                    if (item == tmpItem) Color.BLACK else itemView.context.getColor(R.color.colorTabLayout)
                )

                setOnClickListener {
                    setBackgroundColor(Color.BLACK)
                    tmpItem = item
                    if (onClick != null) {
                        onClick(item,adapterPosition)
                    }
                    notifyDataSetChanged()
                }
            }

            binding.executePendingBindings()

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSongLikedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClick) }
    }
}

class SongLikeDIff : DiffUtil.ItemCallback<Song>() {
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

