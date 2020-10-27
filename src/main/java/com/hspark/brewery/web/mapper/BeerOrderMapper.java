package com.hspark.brewery.web.mapper;

import org.mapstruct.Mapper;

import com.hspark.brewery.domain.BeerOrder;
import com.hspark.brewery.web.model.BeerOrderDto;

@Mapper(uses = {DateMapper.class, BeerOrderLineMapper.class})
public interface BeerOrderMapper {
	
	BeerOrderDto beerOrderToDto(BeerOrder beerOrder);
	
	BeerOrder dtoToBeerOrder(BeerOrderDto dto);
}
