package com.abilsayuri.flix.listMovie.data

data class MovieResponse(
    val page: Int,
    val results: List<MovieDt>,
    val total_pages: Int,
    val total_results: Int
)
