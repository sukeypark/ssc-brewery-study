package com.hspark.brewery.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.Beer;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID>{

	Page<Beer> findAllByBeerNameIsLike(String string, Pageable pageable);

	Page<Beer> findAllByBeerName(String beerName, Pageable pageable);

}
