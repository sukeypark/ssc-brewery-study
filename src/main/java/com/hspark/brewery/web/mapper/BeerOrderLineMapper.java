package com.hspark.brewery.web.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.hspark.brewery.domain.BeerOrderLine;
import com.hspark.brewery.web.model.BeerOrderLineDto;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerOrderLineMapperDecorator.class)
public interface BeerOrderLineMapper {
	BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);
	
	BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
	
}
