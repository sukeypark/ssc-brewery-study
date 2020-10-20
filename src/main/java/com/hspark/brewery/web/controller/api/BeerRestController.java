package com.hspark.brewery.web.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hspark.brewery.services.BeerService;
import com.hspark.brewery.web.model.BeerPagedList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BeerRestController {
	
	private final BeerService beerService;
	
	public ResponseEntity<BeerPagedList> listBeers() {
		BeerPagedList beerList = beerService.listBeers();
		return new ResponseEntity<>(beerList, HttpStatus.OK);
	}
}
