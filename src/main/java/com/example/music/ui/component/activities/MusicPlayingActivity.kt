package com.example.music.ui.component.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.music.data.dto.modelSong.Song
import com.example.music.databinding.ActivityMainBinding
import com.example.music.databinding.ActivityMusicPlayerBinding
import com.example.music.databinding.FragmentPlaylistBinding
import com.example.music.ui.base.BaseActivity
import java.io.IOException

class MusicPlayingActivity : BaseActivity<ActivityMusicPlayerBinding>() {
    private var mediaPlayer: MediaPlayer? = null
    override fun getViewBinding(): ActivityMusicPlayerBinding {
        return ActivityMusicPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Retrieve the data from the intent
        val song = intent.getParcelableExtra<Song>("song_object")

        // hiển thị
        binding.tvSongName.text = song!!.name
        binding.tvArtistName.text = song!!.artist_name
        Glide.with(binding.imgSong).load(song!!.image).into(binding.imgSong)
        Log.d("SSSD","song.id"+song!!.id+" , song.nanme : "+song!!.name)

        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(song.audio)
            mediaPlayer!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Bắt đầu phát nhạc
        mediaPlayer!!.start()

    }


    override fun initView() {
        super.initView()
    }

}