@file:Suppress("DEPRECATION")

package com.abilsayuri.flix.ui

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.abilsayuri.flix.R
import com.abilsayuri.flix.adapter.FilmList
import com.abilsayuri.flix.adapter.MovieItem
import com.abilsayuri.flix.adapter.Slider
import com.abilsayuri.flix.databinding.ActivityMainBinding
import com.abilsayuri.flix.listMovie.data.MovieDt
import com.abilsayuri.flix.local.UserPreferences
import com.abilsayuri.flix.viewModel.MovieViewModel

class MainActivity : AppCompatActivity(), MovieItem {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var sliderAdapter: Slider
    private lateinit var viewPager: ViewPager2
    private lateinit var recyclerViewPopularMovies: RecyclerView
    private lateinit var recyclerViewUpcomingMovies: RecyclerView
    private val slideHandler = Handler()

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("MainActivity", "connectivityReceiver.onReceive called")

            if (isNetworkConnected()) {
                Log.e("MainActivity", "Internet connection is back!")
                refreshData()
            }else if (!isNetworkConnected()) {
                Log.e("MainActivity", "No internet connection")
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)

        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mb.root)

        mb.tvUsername.text = UserPreferences(this).getUser().username

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        viewPager = findViewById(R.id.slider)
        recyclerViewPopularMovies = findViewById(R.id.recyclerView1)
        recyclerViewPopularMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpcomingMovies = findViewById(R.id.recyclerView2)
        recyclerViewUpcomingMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        movieViewModel.networkError.observe(this) { errorMessage ->
            errorMessage?.let {
                if (!isNetworkConnected()) {
                    Toast.makeText(this, "No internet connection !", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity","No internet connection")

                } else {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity","errorMessage")

                }
            }
        }

        movieViewModel.nowPlayingMovies.observe(this, Observer { movies ->
            if (movies != null) {
                sliderAdapter = Slider(movies, viewPager)
                viewPager.adapter = sliderAdapter
                sliderAdapter.attachToViewPager()

                viewPager.clipToPadding = false
                viewPager.clipChildren = false
                viewPager.offscreenPageLimit = 3
                viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                val compositePageTransformer = CompositePageTransformer()
                compositePageTransformer.addTransformer(MarginPageTransformer(48))
                compositePageTransformer.addTransformer { page, position ->
                    val r = 1 - Math.abs(position)
                    page.scaleY = 0.85f + r * 0.15f
                }
                viewPager.setPageTransformer(compositePageTransformer)

                viewPager.setCurrentItem(1, false)

                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        slideHandler.removeCallbacksAndMessages(null)
                        slideHandler.postDelayed(sliderRunnable, Slider.AUTO_SLIDE_INTERVAL)
                    }
                })

                sliderAdapter.startAutoSlide()
            }
        })



        movieViewModel.popularMovies.observe(this, Observer { movies ->
            if (movies != null) {
                recyclerViewPopularMovies.adapter = FilmList(this@MainActivity, movies, this)
            }
        })

        movieViewModel.upcomingMovies.observe(this, Observer { movies ->
            if (movies != null) {
                recyclerViewUpcomingMovies.adapter = FilmList(this@MainActivity, movies, this)
            }
        })


        if (isNetworkConnected()) {

            movieViewModel.fetchNowPlayingMovies()
            movieViewModel.fetchPopularMovies()
            movieViewModel.fetchUpcomingMovies()

        }
    }

    private val sliderRunnable = Runnable {
        viewPager.setCurrentItem(viewPager.currentItem + 1, true)
    }

    override fun onMovieClick(movie: MovieDt, movieImageView: ImageView) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("title", movie.title)
        intent.putExtra("imgURL", movie.getFullPosterPath())
        intent.putExtra("imgCover", movie.getFullBackdropPath())
        intent.putExtra("id", movie.id)

        val options = ActivityOptions.makeSceneTransitionAnimation(this, movieImageView, "sharedName")
        startActivity(intent, options.toBundle())
    }

    private fun isNetworkConnected(): Boolean {
        try {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkCapabilities =
                connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun refreshData() {
        movieViewModel.refreshDataMain()

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        sliderAdapter.stopAutoSlide()
    }

}