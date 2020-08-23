package com.example.movieviewer.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.movieviewer.databinding.FragmentMovieDetailBinding
import com.example.movieviewer.network.model.MovieModel

class MovieDetailFragment : Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val binding = FragmentMovieDetailBinding.inflate(layoutInflater)
		val viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

		binding.lifecycleOwner = this
		binding.viewModel = viewModel

		val movie = requireArguments().getParcelable<MovieModel>("movie")
		viewModel.onCreated(movie!!)

		viewModel.navigateToList.observe(viewLifecycleOwner, Observer {
			it?.let {
				val navController = this.findNavController()

				// if you can go back, then go back to preserve where you were on the browse list, otherwise navigate there as normal
				if (navController.currentDestination != null) {
					navController.navigateUp()
				} else {
					navController.navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToBrowseMoviesFragment6())
				}
			}
		})

		return binding.root
	}
}