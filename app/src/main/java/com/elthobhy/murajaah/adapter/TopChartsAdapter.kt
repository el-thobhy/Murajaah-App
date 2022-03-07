package com.elthobhy.murajaah.adapter

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elthobhy.murajaah.databinding.ItemSongBinding
import com.elthobhy.murajaah.models.Song

class TopChartsAdapter: RecyclerView.Adapter<TopChartsAdapter.ViewHolder>() {

    private var songs = mutableListOf<Song>()
    private var listener:((MutableList<Song>, Int)->Unit)? = null

    inner class ViewHolder(private val itemSongBinding: ItemSongBinding): RecyclerView.ViewHolder(itemSongBinding.root) {
        fun bind(song: Song, listener: ((MutableList<Song>, Int) -> Unit)?) {
            itemSongBinding.tvIndexSong.text = (adapterPosition+1).toString()
            itemSongBinding.tvArtistSong.text = song.artistSong
            itemSongBinding.tvTitleSong.text = song.nameSong

            itemView.setOnClickListener {
                if(listener!=null){
                    listener(songs, adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemSongBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position], listener)
    }

    override fun getItemCount(): Int = songs.size

    fun onClick(listener: ((MutableList<Song>, Int) -> Unit)){
        this.listener = listener
    }

    fun setData(songs: MutableList<Song>){
        this.songs = songs
        notifyDataSetChanged()
    }
}