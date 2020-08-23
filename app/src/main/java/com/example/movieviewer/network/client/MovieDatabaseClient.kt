package com.example.movieviewer.network.client

import com.example.movieviewer.network.model.GenresResponse
import com.example.movieviewer.network.model.MoviesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDatabaseClient {
	@GET("movie/popular")
	fun getPopularMoviesAsync(@Query("page") pageNumber: Long): Deferred<MoviesResponse>

	@GET("genre/movie/list")
	fun getGenresAsync(): Deferred<GenresResponse>
}
