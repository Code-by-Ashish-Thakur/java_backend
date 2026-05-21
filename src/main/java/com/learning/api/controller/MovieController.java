package com.learning.api.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.cache.annotation.Cacheable;

import com.learning.api.dto.MovieRequestDTO;
import com.learning.api.dto.MovieResponseDTO;
import com.learning.api.dto.MoviePageResponseDTO;
import com.learning.api.entity.Movies;
import com.learning.api.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // POST /api/movies
    @PostMapping
    public MovieResponseDTO createMovie(@Valid @RequestBody MovieRequestDTO requestDTO) {
        return new MovieResponseDTO("Movie saved successfully", movieService.createMovie(requestDTO));
    }

    // GET /api/movies (with search, filter & pagination)
    @Cacheable(value = "allMovies",
               key = "'name_' + #name + '_price_' + #price + '_location_' + #location + '_page_' + #page + '_size_' + #size")
    @GetMapping
    public MoviePageResponseDTO getAllMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Movies> moviesPage = movieService.getAllMovies(name, price, location, page, size);

        return new MoviePageResponseDTO(
                "Found " + moviesPage.getTotalElements() + " movie(s)",
                moviesPage.getContent(),
                moviesPage.getNumber(),
                moviesPage.getTotalPages(),
                moviesPage.getTotalElements()
        );
    }

    // GET /api/movies/{id}
    @GetMapping("/{id}")
    public MovieResponseDTO getMovieById(@PathVariable Long id) {
        Optional<Movies> movie = movieService.getMovieById(id);
        if (movie.isPresent()) {
            return new MovieResponseDTO("Movie found successfully", movie.get());
        }
        return new MovieResponseDTO("Movie not found with id: " + id, null);
    }

    // PUT /api/movies/{id}
    @PutMapping("/{id}")
    public MovieResponseDTO updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequestDTO requestDTO) {
        Optional<Movies> movie = movieService.updateMovie(id, requestDTO);
        if (movie.isPresent()) {
            return new MovieResponseDTO("Movie updated successfully", movie.get());
        }
        return new MovieResponseDTO("Movie not found with id: " + id, null);
    }

    // DELETE /api/movies/{id}
    @DeleteMapping("/{id}")
    public MovieResponseDTO deleteMovie(@PathVariable Long id) {
        Optional<Movies> movie = movieService.deleteMovie(id);
        if (movie.isPresent()) {
            return new MovieResponseDTO("Movie deleted successfully", movie.get());
        }
        return new MovieResponseDTO("Movie not found with id: " + id, null);
    }
}
