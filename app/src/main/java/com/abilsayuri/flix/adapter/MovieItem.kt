package com.abilsayuri.flix.adapter

import android.widget.ImageView
import com.abilsayuri.flix.listMovie.data.MovieDt

interface MovieItem {
    fun onMovieClick(movie: MovieDt, movieImageView: ImageView)
}