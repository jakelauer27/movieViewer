package com.example.movieviewer.network.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieModel(
	val id: Long,
	val title: String,
	@Json(name = "vote_average")
	val userRating: Double,
	@Json(name = "vote_count")
	val ratingCount: Long,
	@Json(name = "poster_path")
	val imageUrl: String? = null,
	@Json(name = "genre_ids")
	val genreIds: List<Long>,
	var genreNames: List<String?> = emptyList(),
	val overview: String
) : Parcelable