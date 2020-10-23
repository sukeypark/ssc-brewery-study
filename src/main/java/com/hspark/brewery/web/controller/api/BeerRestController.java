package com.hspark.brewery.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

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

import com.hspark.brewery.services.BeerService;
import com.hspark.brewery.web.model.BeerDto;
import com.hspark.brewery.web.model.BeerPagedList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BeerRestController {
	
	private final BeerService beerService;
	
	@PreAuthorize("hasAuthority('beer.read')")
	@GetMapping(produces = {"application/json"}, path = "beer")
	public ResponseEntity<BeerPagedList> listBeers() {
		BeerPagedList beerList = beerService.listBeers();
		return new ResponseEntity<>(beerList, HttpStatus.OK);
	}
	
	@GetMapping(path = {"beer/{beerId}"}, produces = {"application/json"})
	public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
			@RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        return new ResponseEntity<>(beerService.findBeerById(beerId, showInventoryOnHand), HttpStatus.OK);		
	}
	
	@PreAuthorize("hasAuthority('beer.read')")
	@GetMapping(path = {"beerUpc/{upc}"}, produces = {"application/json"})
	public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
		return new ResponseEntity<>(beerService.findBeerByUpc(upc), HttpStatus.OK);		
	}
	
	@PreAuthorize("hasAuthority('beer.create')")
	@PostMapping(path = "beer")
	public ResponseEntity saveNewBeer(@Valid @RequestBody BeerDto beerDto) {
		
		BeerDto savedDto = beerService.saveBeer(beerDto);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add("Location", "/api/v1/beer_service/" + savedDto.getId().toString());
		
		return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('beer.update')")
	@PutMapping(path = {"beer/{beerId}"}, produces = { "application/json" })
	public ResponseEntity updateBeer(@PathVariable UUID beerId, @Valid @RequestBody BeerDto beerDto) {
		
		beerService.updateBeer(beerId, beerDto);
		
		return new ResponseEntity<> (HttpStatus.NO_CONTENT);
	}
	
	@PreAuthorize("hasAuthority('beer.delete')")
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
