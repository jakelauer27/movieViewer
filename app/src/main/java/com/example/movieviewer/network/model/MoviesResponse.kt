package com.example.movieviewer.network.model

import com.squareup.moshi.Json

data class MoviesResponse(
	val page: Long,
	@Json(name = "total_pages")
	val totalPages: Long,
	@Json(name = "results")
	val movies: List<MovieModel>
)
