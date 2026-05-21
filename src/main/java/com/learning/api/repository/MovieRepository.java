package com.learning.api.repository;

import com.learning.api.entity.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movies, Long> {

    Page<Movies> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Movies> findByPrice(int price, Pageable pageable);
    Page<Movies> findByLocation(String location, Pageable pageable);
    Page<Movies> findByPriceAndLocation(int price, String location, Pageable pageable);
}
