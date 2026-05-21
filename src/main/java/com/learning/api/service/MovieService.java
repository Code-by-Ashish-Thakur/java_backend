package com.learning.api.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.learning.api.dto.MovieRequestDTO;
import com.learning.api.entity.Movies;
import com.learning.api.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // CREATE
    @CacheEvict(value = "allMovies", allEntries = true)
    public Movies createMovie(MovieRequestDTO requestDTO) {
        Movies movie = new Movies(
                requestDTO.getName(),
                requestDTO.getPrice(),
                requestDTO.getLocation()
        );
        return movieRepository.save(movie);
    }

    // GET ALL with search, filter & pagination
    public Page<Movies> getAllMovies(String name, Integer price, String location, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        if (name != null) {
            return movieRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (price != null && location != null) {
            return movieRepository.findByPriceAndLocation(price, location, pageable);
        } else if (price != null) {
            return movieRepository.findByPrice(price, pageable);
        } else if (location != null) {
            return movieRepository.findByLocation(location, pageable);
        } else {
            return movieRepository.findAll(pageable);
        }
    }

    // GET BY ID
    @Cacheable(value = "movies", key = "#id", unless = "#result == null || !#result.isPresent()")
    public Optional<Movies> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // UPDATE
    @Caching(evict = {
        @CacheEvict(value = "movies", key = "#id"),
        @CacheEvict(value = "allMovies", allEntries = true)
    })
    public Optional<Movies> updateMovie(Long id, MovieRequestDTO requestDTO) {
        Optional<Movies> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            Movies existing = movie.get();
            existing.setName(requestDTO.getName());
            existing.setPrice(requestDTO.getPrice());
            existing.setLocation(requestDTO.getLocation());
            return Optional.of(movieRepository.save(existing));
        }
        return Optional.empty();
    }

    // DELETE
    @Caching(evict = {
        @CacheEvict(value = "movies", key = "#id"),
        @CacheEvict(value = "allMovies", allEntries = true)
    })
    public Optional<Movies> deleteMovie(Long id) {
        Optional<Movies> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            movieRepository.deleteById(id);
        }
        return movie;
    }
}
