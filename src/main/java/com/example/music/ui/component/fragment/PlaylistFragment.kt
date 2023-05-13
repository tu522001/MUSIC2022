package com.example.music.ui.component.fragment

import android.media.MediaPlayer
import android.util.Log
import com.bumptech.glide.Glide
import com.example.music.data.dto.modelSong.Song
import com.example.music.databinding.ActivityMusicPlayerBinding
import com.example.music.databinding.FragmentHomeBinding
import com.example.music.databinding.FragmentPlaylistBinding
import com.example.music.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {

    override fun getDataBinding(): FragmentPlaylistBinding {
        return  FragmentPlaylistBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

    }
}