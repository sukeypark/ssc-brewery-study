package com.hspark.brewery.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hspark.brewery.domain.Brewery;
import com.hspark.brewery.services.BreweryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/brewery")
@Controller
public class BreweryController {
	
	private final BreweryService breweryService;
	
	@GetMapping({"/breweries", "/breweries/index", "/breweries/index.html", "/breweries.html"})
	public String listBreweries(Model model) {
		model.addAttribute("breweries", breweryService.getAllBreweries());
		return "breweries/index";
	}
	
    @GetMapping("/api/v1/breweries")
    public @ResponseBody
    List<Brewery> getBreweriesJson(){
        return breweryService.getAllBreweries();
    }
}
