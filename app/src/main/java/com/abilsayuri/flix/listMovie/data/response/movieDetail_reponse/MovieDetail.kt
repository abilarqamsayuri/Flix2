package com.abilsayuri.flix.listMovie.data.response.movieDetail_reponse

import com.abilsayuri.flix.listMovie.data.response.ApiService

data class MovieDetail(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
){
    fun getFullBackdropPath(): String? {
        return backdrop_path?.let { "${ApiService.IMAGE_BASE_URL}$it" }
    }

    fun getFullPosterPath(): String? {
        return poster_path?.let { "${ApiService.IMAGE_BASE_URL}$it" }
    }
}
