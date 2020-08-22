package com.example.movieviewer.browsemovies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.api.client.ApiClientManager
import com.example.movieviewer.api.model.MovieModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

enum class MoviesRequestStatus {
	LOADING,
	ERROR,
	FINISHED
}

class BrowseMoviesViewModel : ViewModel() {
	private var viewModelJob = Job()
	private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

	private val _requestStatus = MutableLiveData<MoviesRequestStatus>()
	private val _movies = MutableLiveData<List<MovieModel>>()

	val requestStatus: LiveData<MoviesRequestStatus>
		get() = _requestStatus
	val movies: LiveData<List<MovieModel>>
		get() =_movies

	init {
		getPopularMovies()
	}

	private fun getPopularMovies() {
		coroutineScope.launch {
			val getMoviesDeferred = ApiClientManager.movieClient.getPopularMovies()

			try {
				_requestStatus.value = MoviesRequestStatus.LOADING
				_movies.value = getMoviesDeferred.await().movies
				_requestStatus.value = MoviesRequestStatus.FINISHED
			} catch (e: Exception) {
				_requestStatus.value = MoviesRequestStatus.ERROR
				_movies.value = emptyList()

				Log.e("Unable to load movies", e.message ?: e.toString())
			}
		}
	}
}