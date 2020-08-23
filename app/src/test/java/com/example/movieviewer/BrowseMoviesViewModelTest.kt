package com.example.movieviewer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieviewer.network.client.MovieDatabaseClient
import com.example.movieviewer.network.model.GenreModel
import com.example.movieviewer.network.model.GenresResponse
import com.example.movieviewer.network.model.MovieModel
import com.example.movieviewer.network.model.MoviesResponse
import com.example.movieviewer.ui.browsemovies.BrowseMoviesViewModel
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.times


@ExperimentalCoroutinesApi
class BrowseMoviesViewModelTest {
	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var browseMoviesViewModel: BrowseMoviesViewModel
	private lateinit var mockMovieClient: MovieDatabaseClient

	private lateinit var movie1: MovieModel
	private lateinit var movie2: MovieModel
	private lateinit var movie3: MovieModel
	private lateinit var movie4: MovieModel

	private lateinit var genre1: GenreModel
	private lateinit var genre2: GenreModel
	private lateinit var genre3: GenreModel

	private val testCoroutineDispatcher = TestCoroutineDispatcher()

	@Before
	fun setup() {
		Dispatchers.setMain(testCoroutineDispatcher)

		mockMovieClient = mock(MovieDatabaseClient::class.java)

		movie1 = MovieModel(
			id = 1,
			title = "What We Do In The Shadows",
			userRating = 10.0,
			overview = "A fantastic comedy about kiwi vampires and vampire life",
			ratingCount = 142,
			genreIds = listOf(1, 3)
		)

		movie2 = movie1.copy(
			id = 2,
			title = "James Band",
			genreIds = listOf(2)
		)

		movie3 = movie1.copy(
			id = 3,
			title = "60 Min. of Nothing",
			genreIds = emptyList()
		)

		movie4 = movie1.copy(
			id = 4,
			title = "Austin Powers",
			genreIds = listOf(1)
		)

		genre1 = GenreModel(id = 1, name = "Super Funny")
		genre2 = GenreModel(id = 2, name = "Action Musical")
		genre3 = GenreModel(id = 3, name = "Not Lame")

		val defferedMoviesPage1 = CompletableDeferred(
			MoviesResponse(
				page = 1,
				movies = listOf(movie1, movie2),
				totalPages = 23
			)
		) as Deferred<MoviesResponse>

		val defferedMoviesPage2 = CompletableDeferred(
			MoviesResponse(
				page = 2,
				movies = listOf(movie3, movie4),
				totalPages = 23
			)
		) as Deferred<MoviesResponse>

		val defferedGenres = CompletableDeferred(
			GenresResponse(
				genres = listOf(genre1, genre2, genre3)
			)
		) as Deferred<GenresResponse>

		whenever(mockMovieClient.getGenresAsync())
			.thenReturn(defferedGenres)

		whenever(mockMovieClient.getPopularMoviesAsync(1))
			.thenReturn(defferedMoviesPage1)

		whenever(mockMovieClient.getPopularMoviesAsync(2))
			.thenReturn(defferedMoviesPage2)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
		testCoroutineDispatcher.cleanupTestCoroutines()
	}

	@Test
	fun `getPopularMovies-should-request-popular-movies-and-genres`() {
		browseMoviesViewModel = BrowseMoviesViewModel(mockMovieClient)

		val expected = listOf(
			movie1.copy(genreNames = listOf(genre1.name, genre3.name)),
			movie2.copy(genreNames = listOf(genre2.name))
		)

		assertThat(browseMoviesViewModel.movies.value).isEqualTo(expected)
		verify(mockMovieClient, times(1)).getPopularMoviesAsync(1)
		verify(mockMovieClient, times(1)).getGenresAsync()
	}

	@Test
	fun `getPopularMovies-should-request-popular-movies-without-genres-when-adding-additional-movies`() {
		browseMoviesViewModel = BrowseMoviesViewModel(mockMovieClient)

		browseMoviesViewModel.loadAdditionalMoviesToEndOfList()

		val expected = listOf(
			movie1.copy(genreNames = listOf(genre1.name, genre3.name)),
			movie2.copy(genreNames = listOf(genre2.name)),
			movie3.copy(genreNames = emptyList()),
			movie4.copy(genreNames = listOf(genre1.name))
		)

		assertThat(browseMoviesViewModel.movies.value).isEqualTo(expected)
		verify(mockMovieClient, times(1)).getPopularMoviesAsync(1)
		verify(mockMovieClient, times(1)).getPopularMoviesAsync(2)
		verify(mockMovieClient, times(1)).getGenresAsync()
	}
}