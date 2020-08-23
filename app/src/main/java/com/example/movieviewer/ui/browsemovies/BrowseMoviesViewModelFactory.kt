package com.example.movieviewer.ui.browsemovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.network.client.MovieDatabaseClient

class BrowseMoviesViewModelFactory(
	private val movieClient: MovieDatabaseClient
) : ViewModelProvider.Factory {
	@Suppress("unchecked_cast")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(BrowseMoviesViewModel::class.java)) {
			return BrowseMoviesViewModel(movieClient) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}