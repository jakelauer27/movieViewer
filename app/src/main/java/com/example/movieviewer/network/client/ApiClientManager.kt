package com.example.movieviewer.network.client

import com.example.movieviewer.BuildConfig

private const val MOVIE_DATABASE_BASE_URL = "https://api.themoviedb.org/3/"
private const val MOVIE_DATABASE_API_KEY_VALUE = BuildConfig.MOVIE_DATABASE_API_KEY
private const val MOVIE_DATABASE_API_KEY = "api_key"

object ApiClientManager {
  val movieClient: MovieDatabaseClient by lazy {
    ClientBuilder.buildRetroFitClient(
      apiClient = MovieDatabaseClient::class.java,
      baseUrl = MOVIE_DATABASE_BASE_URL,
      apiQueryParams = Pair(MOVIE_DATABASE_API_KEY, MOVIE_DATABASE_API_KEY_VALUE)
    )
  }
}