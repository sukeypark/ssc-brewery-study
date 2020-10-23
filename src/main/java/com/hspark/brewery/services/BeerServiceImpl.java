package com.hspark.brewery.services;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.web.mapper.BeerMapper;
import com.hspark.brewery.web.model.BeerDto;
import com.hspark.brewery.web.model.BeerPagedList;
import com.hspark.brewery.web.model.BeerStyleEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
	
	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;

	@Override
	public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest,
			Boolean showInventoryOnHand) {

        log.debug("Listing Beers");

        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            //search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search beer_service style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventoryOnHand) {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());

        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }
        return beerPagedList;
	}

	@Override
	public BeerDto findBeerById(UUID beerId, Boolean showInventoryOnHand) {

		log.debug("Finding Beer by id: " + beerId);
		
		Optional<Beer> beerOptional = beerRepository.findById(beerId);
		
		if (beerOptional.isPresent()) {
			log.debug("Found BeerId: " + beerId);
			
			if (showInventoryOnHand) {
				return beerMapper.beerToBeerDto(beerOptional.get());
			} else {
				return beerMapper.beerToBeerDto(beerOptional.get());				
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. UUID: " + beerId);
		}
	}

	@Override
	public BeerDto findBeerByUpc(String upc) {
		return beerMapper.beerToBeerDto(beerRepository.findByUpc(upc));
	}

	@Override
	public void deleteById(UUID beerId) {
		beerRepository.deleteById(beerId);
		
	}

	@Override
	public BeerDto saveBeer(BeerDto beerDto) {
		return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
	}

	@Override
	public void updateBeer(UUID beerId, BeerDto beerDto) {
		Optional<Beer> beerOptional = beerRepository.findById(beerId);
		
		beerOptional.ifPresentOrElse(beer -> {
			beer.setBeerName(beerDto.getBeerName());
			beer.setBeerStyle(beerDto.getBeerStyle());
			beer.setPrice(beerDto.getPrice());
			beer.setUpc(beerDto.getUpc());
			beerRepository.save(beer);
		}, () -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found. UUID: " + beerId);
		});
		
	}

}
