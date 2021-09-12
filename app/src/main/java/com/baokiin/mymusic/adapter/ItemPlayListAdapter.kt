package com.baokiin.mymusic.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.ItemPlayListBinding
import com.baokiin.mymusic.utils.Utils.hideKeyboard
import kotlinx.android.synthetic.main.item_play_list.view.*


class ItemPlayListAdapter(private val onClick: (Song) -> Unit) :
    ListAdapter<Song, ItemPlayListAdapter.ViewHolder>(
        TitleDIff()
    ) {
    var tmpItem: Song? = null
    inner class ViewHolder(private val binding: ItemPlayListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Song, onClick: ((Song) -> Unit)? = null) {
            binding.data = item

            itemView.apply {
                setBackgroundColor(
                    if (item == tmpItem) Color.BLACK else Color.argb(
                        0,
                        80,
                        15,
                        15
                    )
                )
                txtPosition.apply {
                    setTextColor(
                        when (adapterPosition) {
                            0 -> Color.BLUE
                            1 -> Color.GREEN
                            2 -> Color.RED
                            else -> Color.WHITE
                        }
                    )
                    text = (adapterPosition + 1).toString()
                }
                setOnClickListener {
                    setBackgroundColor(Color.BLACK)
                    tmpItem = item
                    notifyDataSetChanged()
                    if (onClick != null) {
                        onClick(item)
                    }
                }
            }

            binding.executePendingBindings()

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlayListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClick) }
    }
}

