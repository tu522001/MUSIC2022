package com.example.music.ui.component.fragment

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.data.Resource
import com.example.music.data.dto.modelSong.Song
import com.example.music.data.dto.response.ResponseSong
import com.example.music.databinding.FragmentDownloadBinding
import com.example.music.databinding.FragmentSearchBinding
import com.example.music.ui.base.BaseFragment
import com.example.music.ui.component.adapter.SearchAdapter
import com.example.music.ui.component.viewmodel.HomeViewModel
import com.example.music.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : BaseFragment<FragmentDownloadBinding>() {


    override fun getDataBinding(): FragmentDownloadBinding {
        return FragmentDownloadBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
    }

}
