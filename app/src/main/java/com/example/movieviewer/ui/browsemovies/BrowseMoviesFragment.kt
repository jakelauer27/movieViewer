package com.example.movieviewer.ui.browsemovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieviewer.databinding.FragmentBrowseMoviesBinding
import com.example.movieviewer.ui.adapters.MovieAdapter
import com.example.movieviewer.ui.adapters.MovieClickListener
import com.example.movieviewer.ui.constants.MoviesRequestStatus

class BrowseMoviesFragment : Fragment() {
	private val viewModel: BrowseMoviesViewModel by lazy {
		ViewModelProvider(this).get(BrowseMoviesViewModel::class.java)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val binding = FragmentBrowseMoviesBinding.inflate(layoutInflater)

		val gridLayoutManager = GridLayoutManager(context, 2)

		val movieGridAdapter = MovieAdapter(MovieClickListener { movie ->
			movie.let {
				this.findNavController().navigate(
					BrowseMoviesFragmentDirections.actionBrowseMoviesFragmentToMovieDetailFragment(movie)
				)
			}
		})

		binding.lifecycleOwner = this
		binding.viewModel = viewModel
		binding.moviesGrid.adapter = movieGridAdapter
		binding.moviesGrid.layoutManager = gridLayoutManager

		addScrollListenerToMoviesList(binding, gridLayoutManager)

		return binding.root
	}


	private fun addScrollListenerToMoviesList(
		binding: FragmentBrowseMoviesBinding,
		layoutManager: GridLayoutManager
	) {
		binding.moviesGrid.addOnScrollListener(
			object : RecyclerView.OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)

					if (viewModel.requestStatus.value != MoviesRequestStatus.LOADING) {
						if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1) {
							// load more movies to create infinite scroll feel

							/*
								Not completely sure on what android best practices are for this ->
								considered removing pages from the beginning of the list after a certain point
								and reloading them when a user scrolls back up. Could be a good future implementation
								if reviewer thinks it's necessary
							 */
							viewModel.loadAdditionalMoviesToEndOfList()
						}
					}
				}
			}
		)
	}
}