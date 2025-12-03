package com.example.test_lab_week_12.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.network.MovieService

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "f635d942bcf688f8439a6ad574c2fb3b"

    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    suspend fun fetchMovies() {
        try {
            val popularMovies = movieService.getPopularMovies(apiKey)
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            errorLiveData.postValue("An error occurred: ${exception.message}")
        }
    }
}
