package com.hspark.brewery.web.controller.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping(path = {"beerUpc/{upc}"}, produces = {"application/json"})
	public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
		return new ResponseEntity<>(beerService.findBeerByUpc(upc), HttpStatus.OK);		
	}
	
	@DeleteMapping("/beer/{beerId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBeer(@PathVariable("beerId") UUID beerId) {
		beerService.deleteById(beerId);
	}
}
