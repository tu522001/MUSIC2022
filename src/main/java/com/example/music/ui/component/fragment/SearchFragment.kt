package com.example.music.ui.component.fragment

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.data.Resource
import com.example.music.data.dto.modelSong.Genres
import com.example.music.data.dto.modelSong.Song
import com.example.music.data.dto.response.ResponseSong
import com.example.music.databinding.FragmentSearchBinding
import com.example.music.ui.base.BaseFragment
import com.example.music.ui.component.adapter.GenresAdapter
import com.example.music.ui.component.adapter.SearchAdapter
import com.example.music.ui.component.adapter.TopDownloadAdapter
import com.example.music.ui.component.viewmodel.HomeViewModel
import com.example.music.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val searchViewModel by activityViewModels<HomeViewModel>()
    private var search: String? = null
    private var searchList: List<Song> = listOf()
    private lateinit var searchAdapter: SearchAdapter

    private var searchJob: Job? = null

    override fun getDataBinding(): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        val linearLayoutManager = LinearLayoutManager(this.requireContext())

        binding.recyclerViewSearch.layoutManager = linearLayoutManager

        searchAdapter = SearchAdapter(this.requireContext(), searchList.toMutableList())
        binding.recyclerViewSearch.adapter = searchAdapter

        binding.recyclerViewSearch.setHasFixedSize(true)
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.edtSeach.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Hủy job cũ nếu có
                searchJob?.cancel()

                // Tạo job mới
                searchJob = lifecycleScope.launch {
                    // Delay 500ms để debounce tìm kiếm
                    delay(500)
                    // Lấy keyword tìm kiếm
                    search = s.toString()
                    // Thực hiện tìm kiếm
                    searchViewModel.getSearchSongs(1, 5, search!!)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun addObservers() {
        super.addObservers()
        observe(searchViewModel.searchSongs, ::handleSearchSong)
    }

    private fun handleSearchSong(resource: Resource<ResponseSong>) {
        if (resource.data != null) {
            resource.data.let {
                searchList = it.data
                searchAdapter.updateData(searchList)
            }
        }
    }
}
