package com.example.movieviewer.ui.browsemovies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.network.client.ApiClientManager
import com.example.movieviewer.network.model.GenresResponse
import com.example.movieviewer.network.model.MovieModel
import com.example.movieviewer.network.model.MoviesResponse
import com.example.movieviewer.ui.constants.MoviesRequestStatus
import kotlinx.coroutines.*
import java.lang.Exception

class BrowseMoviesViewModel : ViewModel() {
	private lateinit var genres: Map<Long, String>

	private var currentMoviePage = 1L
	private var maxPages = 1L

	private val viewModelJob = Job()
	private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
	private val _requestStatus = MutableLiveData<MoviesRequestStatus>()
	private val _movies = MutableLiveData<List<MovieModel>>()

	val requestStatus: LiveData<MoviesRequestStatus>
		get() = _requestStatus
	val movies: LiveData<List<MovieModel>>
		get() = _movies

	init {
		getPopularMovies(
			pageIndex = currentMoviePage,
			getGenres = true
		)
	}

	fun loadAdditionalMoviesToEndOfList() {
		currentMoviePage++
		getPopularMovies(
			pageIndex = currentMoviePage,
			getGenres = false
		)
	}

	private fun getPopularMovies(
		pageIndex: Long,
		getGenres: Boolean
	) {
		_requestStatus.value = MoviesRequestStatus.LOADING

		if (pageIndex <= maxPages) {
			coroutineScope.launch {
				val getMoviesDeferred = ApiClientManager.movieClient.getPopularMoviesAsync(pageIndex)
				val getGenresDeferred = if (getGenres) ApiClientManager.movieClient.getGenresAsync() else null

				try {
					val moviesResponse = getMoviesDeferred.await()

					if (getGenresDeferred != null) {
						loadGenres(getGenresDeferred.await())
					}

					addNewMoviesToList(moviesResponse)
					maxPages = moviesResponse.totalPages
					_requestStatus.value = MoviesRequestStatus.FINISHED
				} catch (e: Exception) {
					_requestStatus.value = MoviesRequestStatus.ERROR
					_movies.value = emptyList()

					Log.e("Unable to load movies", e.message ?: e.toString())
				}
			}
		}
	}

	private fun addNewMoviesToList(moviesResponse: MoviesResponse) {
		val newMovies = moviesResponse.movies.map { movie ->
			movie.copy(
				genreNames = movie.genreIds.map { genres[it] }
			)
		}

		_movies.value = _movies.value.orEmpty().plus(newMovies)
	}

	private fun loadGenres(genresResponse: GenresResponse) {
		genres = genresResponse.genres.map { it.id to it.name}.toMap()
	}

	override fun onCleared() {
		super.onCleared()
		viewModelJob.cancel()
	}
}