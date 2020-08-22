package com.example.movieviewer.api.model

import com.squareup.moshi.Json

class MovieModel(
	val id: Long,
	val title: String,
	@Json(name = "release_date")
	val releaseDate: String,
	val overview: String,
	val popularity: Double,
	@Json(name = "vote_average")
	val userRating: Double,
	@Json(name = "genre_ids")
	val genreIds: List<Long>,
	@Json(name = "poster_path")
	val image: String
)