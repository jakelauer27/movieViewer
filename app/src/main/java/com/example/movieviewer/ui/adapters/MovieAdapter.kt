package com.example.movieviewer.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieviewer.databinding.GridViewMovieBinding
import com.example.movieviewer.network.model.MovieModel

class MovieAdapter(
	private val clickListener: MovieClickListener
) : ListAdapter<MovieModel, RecyclerView.ViewHolder>(MovieDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return ViewHolder.from(parent)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is ViewHolder -> {
				val movieItem = getItem(position) as MovieModel
				holder.bind(clickListener, movieItem)
			}
		}
	}

	class ViewHolder private constructor(
		private val binding: GridViewMovieBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(clickListener: MovieClickListener, item: MovieModel) {
			binding.movie = item
			binding.clickListener = clickListener
			binding.executePendingBindings()
		}

		companion object {
			fun from(parent: ViewGroup): ViewHolder {
				val layoutInflater = LayoutInflater.from(parent.context)
				val binding = GridViewMovieBinding.inflate(layoutInflater, parent, false)

				return ViewHolder(binding)
			}
		}
	}
}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
	@SuppressLint("DiffUtilEquals")
	override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
		return oldItem == newItem
	}

	override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
		return oldItem.id == newItem.id
	}
}

class MovieClickListener(val clickListener: (movie: MovieModel) -> Unit) {
	fun onClick(movie: MovieModel) = clickListener(movie)
}