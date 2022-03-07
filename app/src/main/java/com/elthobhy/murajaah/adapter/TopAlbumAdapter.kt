package com.elthobhy.murajaah.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elthobhy.murajaah.databinding.ItemAlbumBinding
import com.elthobhy.murajaah.databinding.ItemSongBinding
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song

class TopAlbumAdapter: RecyclerView.Adapter<TopAlbumAdapter.ViewHolder>() {

    private var albums = mutableListOf<Album>()
    private var listener:((Album)->Unit)? = null

    inner class ViewHolder(private val itemAlbumBinding: ItemAlbumBinding): RecyclerView.ViewHolder(itemAlbumBinding.root) {
        fun bind(album: Album, listener: ((Album) -> Unit)?) {
            Glide.with(itemView).load(album.imageAlbum).placeholder(android.R.color.darker_gray).into(itemAlbumBinding.ivAlbum)
            itemAlbumBinding.tvNameAlbum.text = album.nameAlbum
            itemAlbumBinding.tvArtistAlbum.text = album.artistAlbum

            itemView.setOnClickListener {
                if(listener!=null){
                    listener(album)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemAlbumBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position], listener)
    }

    override fun getItemCount(): Int = albums.size

    fun onClick(listener: ((Album) -> Unit)){
        this.listener = listener
    }

    fun setData(albums: MutableList<Album>){
        this.albums = albums
        notifyDataSetChanged()
    }
}