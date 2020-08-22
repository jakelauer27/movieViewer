package com.example.movieviewer.api.client

import com.example.movieviewer.api.model.GenresResponse
import com.example.movieviewer.api.model.MoviesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDatabaseClient {
	@GET("movie/popular")
	fun getPopularMovies(@Query("page") pageNumber: Int? = 1): Deferred<MoviesResponse>

	@GET("genre/movie/list")
	fun getGenres(): Deferred<GenresResponse>
}
