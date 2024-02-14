package com.abilsayuri.flix.listMovie.data.response

import com.abilsayuri.flix.listMovie.data.MovieResponse
import com.abilsayuri.flix.listMovie.data.response.cast_response.CastResponse
import com.abilsayuri.flix.listMovie.data.response.movieDetail_reponse.MovieDetail
import com.abilsayuri.flix.listMovie.data.response.video_reponse.Video
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieDetail>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieResponse>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<CastResponse>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendationMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieResponse>

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<MovieResponse>

    @GET("movie/{movie_id}/videos")
    fun getVideosList(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String= API_KEY
    ): Call<Video>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "ee9e8598094088f618729b17b2212d3e"
    }

}