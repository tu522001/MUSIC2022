package com.example.music.ui.component.fragment

import com.example.music.databinding.FragmentDownloadBinding
import com.example.music.databinding.FragmentFavoriteBinding
import com.example.music.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment: BaseFragment<FragmentFavoriteBinding>() {


    override fun getDataBinding(): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
    }

}