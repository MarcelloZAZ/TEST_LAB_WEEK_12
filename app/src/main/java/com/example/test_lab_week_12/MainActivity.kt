package com.example.test_lab_week_12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.repeatOnLifecycle
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieAdapter = MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    movie.title ?: "No title",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository

        val movieViewModel = ViewModelProvider(
            this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            }
        )[MovieViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {

                launch {
                    movieViewModel.popularMovies.collect { movieList ->
                        val currentYear =
                            Calendar.getInstance().get(Calendar.YEAR).toString()

                        movieAdapter.addMovies(
                            movieList
                                .filter { it.releaseDate?.startsWith(currentYear) == true }
                                .sortedByDescending { it.popularity }
                        )
                    }
                }

                launch {
                    movieViewModel.error.collect { errorMessage ->
                        if (errorMessage.isNotEmpty()) {
                            Snackbar.make(recyclerView, errorMessage, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
