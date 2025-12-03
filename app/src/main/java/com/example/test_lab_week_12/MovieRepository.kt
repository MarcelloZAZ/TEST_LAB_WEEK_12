package com.example.test_lab_week_12.data

import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "f635d942bcf688f8439a6ad574c2fb3b"

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val response = movieService.getPopularMovies(apiKey)
            emit(response.results ?: emptyList())
        }.flowOn(Dispatchers.IO)
    }
}
