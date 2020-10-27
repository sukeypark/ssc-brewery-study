package com.hspark.brewery.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hspark.brewery.security.perms.BeerCreatePermission;
import com.hspark.brewery.security.perms.BeerDeletePermission;
import com.hspark.brewery.security.perms.BeerReadPermission;
import com.hspark.brewery.security.perms.BeerUpdatePermission;
import com.hspark.brewery.services.BeerService;
import com.hspark.brewery.web.model.BeerDto;
import com.hspark.brewery.web.model.BeerPagedList;
import com.hspark.brewery.web.model.BeerStyleEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class BeerRestController {
	
	private final BeerService beerService;
	
	private static final Integer DEFAULT_PAGE_NUMBER = 0;
	private static final Integer DEFAULT_PAGE_SIZE = 25;
	
	@BeerReadPermission
	@GetMapping(produces = {"application/json"}, path = "beer")
	public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
			                                       @RequestParam(value = "beerName", required = false) String beerName,
			                                       @RequestParam(value = "pageStyle", required = false) BeerStyleEnum beerStyle,
			                                       @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
		
		log.debug("Listing Beers");
		
		if (showInventoryOnHand == null) {
			showInventoryOnHand = false;
		}
		
		if (pageNumber == null || pageNumber < 0) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		
		if (pageSize == null || pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}

		BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
		
		return new ResponseEntity<>(beerList, HttpStatus.OK);
	}
	
	@BeerReadPermission
	@PreAuthorize("hasAuthority('beer.read')")
	@GetMapping(path = {"beer/{beerId}"}, produces = {"application/json"})
	public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
			@RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        return new ResponseEntity<>(beerService.findBeerById(beerId, showInventoryOnHand), HttpStatus.OK);		
	}
	
	@BeerReadPermission
	@PreAuthorize("hasAuthority('beer.read')")
	@GetMapping(path = {"beerUpc/{upc}"}, produces = {"application/json"})
	public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
		return new ResponseEntity<>(beerService.findBeerByUpc(upc), HttpStatus.OK);		
	}
	
	@BeerCreatePermission
	@PostMapping(path = "beer")
	public ResponseEntity saveNewBeer(@Valid @RequestBody BeerDto beerDto) {
		
		BeerDto savedDto = beerService.saveBeer(beerDto);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add("Location", "/api/v1/beer_service/" + savedDto.getId().toString());
		
		return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
	}
	
	
	@BeerUpdatePermission
	@PutMapping(path = {"beer/{beerId}"}, produces = { "application/json" })
	public ResponseEntity updateBeer(@PathVariable UUID beerId, @Valid @RequestBody BeerDto beerDto) {
		
		beerService.updateBeer(beerId, beerDto);
		
		return new ResponseEntity<> (HttpStatus.NO_CONTENT);
	}
	
	@BeerDeletePermission
	@DeleteMapping("/beer/{beerId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBeer(@PathVariable("beerId") UUID beerId) {
		beerService.deleteById(beerId);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<List> badRequestHandler(ConstraintViolationException e) {
		List<String> errors = new ArrayList<>(e.getConstraintViolations().size());
		
		e.getConstraintViolations().forEach(constraintViolation -> {
			errors.add(constraintViolation.getPropertyPath().toString() 
					+ " : " + constraintViolation.getMessage());
		});
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
