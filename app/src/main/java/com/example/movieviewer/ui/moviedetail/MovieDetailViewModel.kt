package com.example.movieviewer.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieviewer.network.model.MovieModel

class MovieDetailViewModel : ViewModel() {
	private var _movie = MutableLiveData<MovieModel>()
	val movie: LiveData<MovieModel>
		get() = _movie

	private var _navigateToList = MutableLiveData<Boolean>()
	val navigateToList: LiveData<Boolean>
		get() = _navigateToList

	fun onCreated(movie: MovieModel) {
		_movie.value = movie
	}

	fun onClose() {
		_navigateToList.value = true
	}
}