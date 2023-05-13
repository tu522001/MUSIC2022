package com.example.music.ui.component.adapter

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.data.dto.modelSong.Song
import com.example.music.databinding.ItemLayoutSearchBinding
import com.example.music.ui.base.listeners.onClickItemListener

class SearchAdapter (var context: Context, var listSearchSong: MutableList<Song>
) :
    RecyclerView.Adapter<SearchAdapter.SearchHolder>() {
    private var mediaPlayer: MediaPlayer? = null

    inner class SearchHolder(val itembinding: ItemLayoutSearchBinding) :
        RecyclerView.ViewHolder(itembinding.root) {
        fun bind(song: Song) {

            itembinding.tvSongNameSearch.text =
                song.name.take(8) + if (song.name.length > 8) "..." else ""
            itembinding.tvSingerNameSearch.text = song.artist_name
            Glide.with(itembinding.root).load(song.image).into(itembinding.imgSearch)

//            itembinding.imgSearch.setOnClickListener {
//                onClickItemListener.onCLick(adapterPosition, song)
//                Log.d(
//                    "RRR",
//                    "song.album_id : " + song.album_id
//                            + "song.album_image : " + song.album_image
//                            + "song.artist_id : " + song.artist_id
//                            + "song.artist_idstr : " + song.artist_idstr
//                            + "song.artist_name : " + song.artist_name
//                            + "song.audio : " + song.audio
//                            + "song.audiodownload : " + song.audiodownload
//                            + "song.audiodownload_allowed : " + song.audiodownload_allowed
//                            + "song.duration : " + song.duration
//                            + "song.id : " + song.id
//                            + "song.image : " + song.image
//                            + "song.license_ccurl : " + song.license_ccurl
//                            + "song.name : " + song.name
//                            + "song.releasedate : " + song.releasedate
//                            + "song.thumbnail : " + song.thumbnail
//                )
//
//            }

        }
    }

    fun updateData(data: List<Song>) {
        listSearchSong.clear()
        listSearchSong.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var itemBinding = ItemLayoutSearchBinding.inflate(layoutInflater, parent, false)
        return SearchHolder(itemBinding)
    }


    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.bind(listSearchSong[position])
    }

    override fun getItemCount(): Int {
        return listSearchSong.size
    }
}