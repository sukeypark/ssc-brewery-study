package com.hspark.brewery.services;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import com.hspark.brewery.web.model.BeerDto;
import com.hspark.brewery.web.model.BeerPagedList;
import com.hspark.brewery.web.model.BeerStyleEnum;

public interface BeerService {

	BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

	BeerDto findBeerById(UUID beerId, Boolean showInventoryOnHand);

	BeerDto saveBeer(BeerDto beerDto);

	void updateBeer(UUID beerId, BeerDto beerDto);

	void deleteById(UUID beerId);

	BeerDto findBeerByUpc(String upc);
}