package com.example.movieviewer.browsemovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieviewer.databinding.FragmentBrowseMoviesBinding

class BrowseMoviesFragment : Fragment() {
	private val viewModel: BrowseMoviesViewModel by lazy {
		ViewModelProvider(this).get(BrowseMoviesViewModel::class.java)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val binding = FragmentBrowseMoviesBinding.inflate(inflater)

		binding.lifecycleOwner = this
		binding.viewModel = viewModel

		return super.onCreateView(inflater, container, savedInstanceState)
	}
}