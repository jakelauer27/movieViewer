package com.example.movieviewer.network.client

import com.example.movieviewer.BuildConfig

private const val MOVIE_DATABASE_BASE_URL = "https://api.themoviedb.org/3/"
private const val MOVIE_DATABASE_API_KEY = BuildConfig.MOVIE_DATABASE_API_KEY

object ApiClientManager {
	val movieClient: MovieDatabaseClient by lazy {
		ClientBuilder.buildRetroFitClient(
			apiClient = MovieDatabaseClient::class.java,
			baseUrl = MOVIE_DATABASE_BASE_URL,
			apiKey = MOVIE_DATABASE_API_KEY
		)
	}
}