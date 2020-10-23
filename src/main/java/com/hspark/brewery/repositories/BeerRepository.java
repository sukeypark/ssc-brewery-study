package com.hspark.brewery.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.web.model.BeerStyleEnum;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID>{

	Page<Beer> findAllByBeerNameIsLike(String string, Pageable pageable);

	Page<Beer> findAllByBeerName(String beerName, Pageable pageable);

	Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest);

	Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, PageRequest pageRequest);

	Beer findByUpc(String upc);

}
