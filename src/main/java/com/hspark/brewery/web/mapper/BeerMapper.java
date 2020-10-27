package com.hspark.brewery.web.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.web.model.BeerDto;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
@DecoratedWith(BeerMapperDecorator.class)
public interface BeerMapper {

	BeerDto beerToBeerDto(Beer beer);

	Beer beerDtoToBeer(BeerDto beerDto);
	
}
