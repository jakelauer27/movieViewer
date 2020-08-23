package com.example.movieviewer.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieviewer.R
import com.example.movieviewer.network.model.MovieModel
import com.example.movieviewer.ui.constants.MoviesRequestStatus
import java.text.DecimalFormat

const val BASE_URL = "https://image.tmdb.org/t/p/"
const val SMALL_IMAGE_WIDTH = "w200"
val decimalFormat = DecimalFormat("0.#")

@BindingAdapter("movies")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MovieModel>?) {
	val adapter = recyclerView.adapter as MovieAdapter
	adapter.submitList(data)
}

@BindingAdapter("movieImage")
fun bindImage(imgView: ImageView, imgUrl: String?) {
	imgUrl?.let {
		val fullUrl = "$BASE_URL/$SMALL_IMAGE_WIDTH/$imgUrl"
		val imgUri = fullUrl.toUri().buildUpon().scheme("https").build()
		Glide.with(imgView.context)
			.load(imgUri)
			.into(imgView)
	}

	if (imgUrl == null) {
		imgView.setImageResource(R.drawable.ic_launcher_foreground)
	}
}

@BindingAdapter("movieRating")
fun TextView.bindRating(movie: MovieModel?) {
	movie?.let {
		text = context.getString(R.string.movie_rating, decimalFormat.format(movie.userRating), movie.ratingCount)
	}
}

@BindingAdapter("moviesRequestStatus")
fun bindStatus(statusImageView: ImageView, status: MoviesRequestStatus?) {
	when (status) {
		MoviesRequestStatus.LOADING -> {
			statusImageView.visibility = View.VISIBLE
			statusImageView.setImageResource(R.drawable.ic_launcher_foreground)
		}
		MoviesRequestStatus.ERROR -> {
			statusImageView.visibility = View.VISIBLE
			statusImageView.setImageResource(R.drawable.ic_launcher_foreground)
		}
		MoviesRequestStatus.FINISHED -> {
			statusImageView.visibility = View.GONE
		}
	}
}

@BindingAdapter("movieGenres")
fun TextView.bindGenres(movieGenres: List<String>?) {
	movieGenres?.let {
		text = movieGenres.foldIndexed("") { index, genresString, genre ->
			if (index == 0) {
				genre
			} else {
				"$genresString, $genre"
			}
		}
	}
}