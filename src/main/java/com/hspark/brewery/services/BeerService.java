package com.hspark.brewery.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hspark.brewery.web.model.BeerDto;
import com.hspark.brewery.web.model.BeerPagedList;

@Service
public class BeerService {

	public BeerPagedList listBeers() {
		// TODO Auto-generated method stub
		return null;
	}

	public BeerDto findBeerById(UUID beerId, Boolean showInventoryOnHand) {
		// TODO Auto-generated method stub
		return null;
	}

	public BeerDto findBeerByUpc(String upc) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteById(UUID beerId) {
		// TODO Auto-generated method stub
		
	}

}
