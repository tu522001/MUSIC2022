package com.example.music.ui.component.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.music.R
import com.example.music.databinding.ActivityMainBinding
import com.example.music.ui.base.BaseActivity
import com.example.music.ui.component.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val releaseViewModel by viewModels<HomeViewModel>()
    private val topTrendingViewModel by viewModels<HomeViewModel>()
    private val topDownLoadViewModel by viewModels<HomeViewModel>()
    private val genresViewModel by viewModels<HomeViewModel>()
    private val photoAboveViewModel by viewModels<HomeViewModel>()

    /** Chuyển hết các ViewModel như ReleaseViewModel, TopTrendingViewModel, GenresViewModel, PhotoAboveViewModel
        gộp lại gọi hết vào cái HomeViewModel để dễ dàng cho việc quản lý ứng dụng */
//    private val releaseViewModel by viewModels<ReleaseViewModel>()
//    private val topTrendingViewModel by viewModels<TopTrendingViewModel>()
//    private val topDownLoadViewModel by viewModels<TopDownLoadViewModel>()
//    private val genresViewModel by viewModels<GenresViewModel>()
//    private val photoAboveViewModel by viewModels<PhotoAboveViewModel>()

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        /** viết ViewModel vào View để dữ liệu cập nhập trực tiếp từ dữ liệu ViewModel trong View nghĩa là
        khi tôi chuyển màn từ Discover sang các màn khác ví dụ : khi tôi chuyển từ màn Discover sang Playlist xong tôi lại chuyển lại
        từ màn Playlist sang màn Discover thì dữ liệu của tôi đã được cập nhật luôn trực tiếp khi vào view rồi không cần phải thông
        qua ViewModel xong với về view nữa */
        releaseViewModel.getNetReleaseSongs(page = 1, limit = 20, "release")
        topTrendingViewModel.getTopTrendingSongs(page = 1, limit = 20, "trending")
        topDownLoadViewModel.getTopDownLoadSongs(page = 1, limit = 5, "download")
        genresViewModel.getGenresSongs()
        photoAboveViewModel.getPhotoAbove()
    }

    override fun initView() {
        super.initView()
        initBottomNavigation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    private fun initBottomNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        navController.addOnDestinationChangedListener { controller, destination, _->
            //handle banner ads state
            if (destination.id != binding.bottomNav.selectedItemId) {
                controller.backQueue.asReversed().drop(1).forEach { entry ->
                    binding.bottomNav.menu.forEach { item ->
                        if (entry.destination.id == item.itemId) {
                            item.isChecked = true
                            return@addOnDestinationChangedListener
                        }
                    }
                }
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId)
            {
                R.id.homeFragment ->  NavigationUI.onNavDestinationSelected(item, navController)
                R.id.playlistFragment ->  NavigationUI.onNavDestinationSelected(item, navController)
                R.id.downloadFragment ->  NavigationUI.onNavDestinationSelected(item, navController)
                R.id.favoriteFragment ->  NavigationUI.onNavDestinationSelected(item, navController)
            }
            true
        }
    }
//    private fun loadFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container, fragment)
//        transaction.commit()
//    }
}