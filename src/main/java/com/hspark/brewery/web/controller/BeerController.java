package com.hspark.brewery.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hspark.brewery.domain.Beer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/beers")
@Controller
public class BeerController {
	
	@RequestMapping("/find")
	public String findBeers(Model model) {
		model.addAttribute("beer", Beer.builder().build());
		return "beers/findBeers";
	}

}
