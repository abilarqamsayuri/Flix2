package com.abilsayuri.flix.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abilsayuri.flix.listMovie.data.response.ApiConfig.movieApi
import com.abilsayuri.flix.listMovie.data.MovieDt
import com.abilsayuri.flix.listMovie.data.MovieResponse
import com.abilsayuri.flix.listMovie.data.response.cast_response.CastResponse
import com.abilsayuri.flix.listMovie.data.response.movieDetail_reponse.MovieDetail
import com.abilsayuri.flix.listMovie.data.response.video_reponse.Video
import com.abilsayuri.flix.listMovie.data.response.video_reponse.VideoDt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _nowPlayingMovies = MutableLiveData<List<MovieDt>>()
    val nowPlayingMovies: LiveData<List<MovieDt>> get() = _nowPlayingMovies

    private val _popularMovies = MutableLiveData<List<MovieDt>>()
    val popularMovies: LiveData<List<MovieDt>> get() = _popularMovies

    private val _upcomingMovies = MutableLiveData<List<MovieDt>>()
    val upcomingMovies: LiveData<List<MovieDt>> get() = _upcomingMovies

    private val _credits = MutableLiveData<CastResponse?>()
    val credits: LiveData<CastResponse?> get() = _credits

    private val _videos = MutableLiveData<List<VideoDt>>()
    val videos: LiveData<List<VideoDt>> get() = _videos

    private val _movieDetail = MutableLiveData<MovieDetail?>()
    val movieDetail: LiveData<MovieDetail?> get() = _movieDetail

    private val _similarMovies = MutableLiveData<List<MovieDt>>()
    val similarMovies: LiveData<List<MovieDt>> get() = _similarMovies

    private val _searchResults = MutableLiveData<List<MovieDt>>()
    val searchResults: LiveData<List<MovieDt>> get() = _searchResults

    private val _networkError = MutableLiveData<String>()
    val networkError: LiveData<String> get() = _networkError


    fun fetchNowPlayingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieApi.getMoviesList("now_playing", 1)
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    _nowPlayingMovies.postValue(movies)
                    Log.d(TAG, "Now Playing Movies fetched successfully: $movies")
                } else {
                    // Handle error
                    _nowPlayingMovies.postValue(emptyList())
                    Log.e(TAG, "Error fetching Now Playing Movies: ${response.code()} - ${response.message()}")
                    Log.e(TAG, "Error response body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during Now Playing Movies fetch: ${e.message}")
            }
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieApi.getMoviesList("popular", 1)
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    _popularMovies.postValue(movies)
                    Log.d(TAG, "Popular Movies fetched successfully: $movies")
                } else {
                    // Handle error
                    _popularMovies.postValue(emptyList())
                    Log.e(TAG, "Error fetching Popular Movies: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during Popular Movies fetch: ${e.message}")
            }
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieApi.getMoviesList("upcoming", 1)
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    _upcomingMovies.postValue(movies)
                    Log.d(TAG, "Upcoming Movies fetched successfully: $movies")
                } else {
                    // Handle error
                    _upcomingMovies.postValue(emptyList())
                    Log.e(TAG, "Error fetching Upcoming Movies: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during Upcoming Movies fetch: ${e.message}")
            }
        }
    }


    fun fetchMovieDetail(movieId: Int) {
        val call = movieApi.getMovieDetail(movieId)

        call.enqueue(object : Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.isSuccessful) {
                    _movieDetail.value = response.body()
                } else {
                    _networkError.value = "Failed to fetch movie details"
                }
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                _networkError.value = "Error: ${t.message}"

            }
        })
    }


    fun fetchMovieCredits(movieId: Int) {
        val call = movieApi.getMovieCredits(movieId)

        call.enqueue(object : Callback<CastResponse> {
            override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                if (response.isSuccessful) {
                    _credits.value = response.body()
                } else {
                    _networkError.value = "Failed to fetch movie credits"
                }
            }

            override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                _networkError.value = "Error: ${t.message}"

            }
        })
    }



    fun fetchVideos(movieId: Int) {
        val call = movieApi.getVideosList(movieId)

        call.enqueue(object : Callback<Video> {
            override fun onResponse(call: Call<Video>, response: Response<Video>) {
                if (response.isSuccessful) {
                    _videos.value = response.body()?.results
                } else {
                    _networkError.value = "Failed to fetch videos"

                }
            }

            override fun onFailure(call: Call<Video>, t: Throwable) {
                _networkError.value = "Error: ${t.message}"
            }
        })
    }




    fun fetchSimilarMovies(movieId: Int) {
        val call = movieApi.getSimilarMovies(movieId)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    _similarMovies.value = response.body()?.results
                } else {
                    _networkError.value = "Failed to fetch similar movies"
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                _networkError.value = "Network error: ${t.message}"
            }
        })
    }


    fun searchMovies(query: String, page: Int) {
        val call = movieApi.searchMovie(query, page)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.results
                } else {
                    _networkError.value = "Failed to fetch search results"
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                _networkError.value = "Network error: ${t.message}"
            }
        })
    }


    fun refreshDataMain() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchNowPlayingMovies()
            fetchPopularMovies()
            fetchUpcomingMovies()
        }

    }


    fun refreshData(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            fetchVideos(id)
            fetchSimilarMovies(id)
            fetchMovieCredits(id)
            fetchMovieDetail(id)
        }
    }


    companion object {
        private const val TAG = "MovieViewModel"
    }
}