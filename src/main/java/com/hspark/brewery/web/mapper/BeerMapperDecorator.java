package com.hspark.brewery.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.domain.BeerInventory;
import com.hspark.brewery.web.model.BeerDto;

public abstract class BeerMapperDecorator implements BeerMapper {
	
	private BeerMapper beerMapper;
	
	@Autowired
	@Qualifier("delegate")
	public void setBeerMapper(BeerMapper beerMapper) {
		this.beerMapper = beerMapper;
	}

	@Override
	public BeerDto beerToBeerDto(Beer beer) {
		
		BeerDto dto = beerMapper.beerToBeerDto(beer);
		
		if (beer.getBeerInventory() != null && beer.getBeerInventory().size() > 0) {
			dto.setQuantityOnHand(beer.getBeerInventory()
					.stream().map(BeerInventory::getQuantityOnHand)
					.reduce(0, Integer::sum));
		}
		
		return dto;
	}	
}
