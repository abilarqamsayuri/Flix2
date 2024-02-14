package com.abilsayuri.flix.listMovie.data

import com.abilsayuri.flix.listMovie.data.response.ApiService

data class MovieDt(
    val adult: Boolean?,
    val backdrop_path: String,
    val genre_ids: List<Int>?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?

){
    fun getFullBackdropPath(): String? {
        return backdrop_path?.let { "${ApiService.IMAGE_BASE_URL}$it" }
    }

    fun getFullPosterPath(): String? {
        return poster_path?.let { "${ApiService.IMAGE_BASE_URL}$it" }
    }
}
