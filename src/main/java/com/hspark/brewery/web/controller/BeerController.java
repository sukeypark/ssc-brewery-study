package com.hspark.brewery.web.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.repositories.BeerInventoryRepository;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.security.perms.BeerCreatePermission;
import com.hspark.brewery.security.perms.BeerReadPermission;
import com.hspark.brewery.security.perms.BeerUpdatePermission;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/beers")
@Controller
public class BeerController {
	
	private final BeerRepository beerRepository;
	private final BeerInventoryRepository beerInventoryRepository;
	
	@BeerReadPermission
	@RequestMapping("/find")
	public String findBeers(Model model) {
		model.addAttribute("beer", Beer.builder().build());
		return "beers/findBeers";
	}
	
	@BeerReadPermission
	@GetMapping
	public String processFindFormReturnMany(Beer beer, BindingResult result, Model model) {
		Page<Beer> pagedResult = beerRepository.findAllByBeerNameIsLike(
				"%" + beer.getBeerName() + "%",
				createPageRequest(0, 10, Sort.Direction.DESC, "beerName"));
		List<Beer> beerList = pagedResult.getContent();
		if (beerList.isEmpty()) {
			result.rejectValue("beerName", "notFound", "not found");
			return "beers/findBeers";
		} else if (beerList.size() == 1) {
			beer = beerList.get(0);
			return "redirect:/beers/" + beer.getId();
		} else {
			model.addAttribute("selections", beerList);
			return "beers/beerList";
		}
	}
	
	@BeerReadPermission
	@GetMapping("/{beerId}")
	public ModelAndView showBeer(@PathVariable UUID beerId) {
		ModelAndView mav = new ModelAndView("beers/beerDetails");
		// TODO: add Service
		mav.addObject(beerRepository.findById(beerId).get());
		return mav;
	}
    
	@BeerCreatePermission
    @GetMapping("/new")
    public String initCreationForm(Model model) {
    	model.addAttribute("beer", Beer.builder().build());
    	return "beers/createBeer";
    }
    
	@BeerCreatePermission
    @PostMapping("/new")
    public String processCreationForm(Beer beer) {
    	//TODO: add service
    	Beer newBeer = Beer.builder()
    			.beerName(beer.getBeerName())
    			.beerStyle(beer.getBeerStyle())
    			.minOnHand(beer.getMinOnHand())
    			.price(beer.getPrice())
    			.quantityToBrew(beer.getQuantityToBrew())
    			.upc(beer.getUpc())
    			.build();
    	
    	Beer savedBeer = beerRepository.save(newBeer);
    	return "redirect:/beers/" + savedBeer.getId();
    }
    
	@BeerUpdatePermission
    @GetMapping("/{beerId}/edit")
    public String initUpdateBeerForm(@PathVariable UUID beerId, Model model) {
    	Optional<Beer> beerById = beerRepository.findById(beerId);
    	
    	if (beerById.isPresent()) {
    		model.addAttribute("beer", beerById.get());
    	}
    	
    	return "beers/createOrUpdateBeer";
    }
    
	@BeerUpdatePermission
    @PostMapping("/{beerId}/edit")
    public String processUpdateForm(@Valid Beer beer, BindingResult result) {
    	if (result.hasErrors()) {
    		return "beers/createOrUpdateBeer";
    	} else {
    		// Todo: add service
    		Beer savedBeer = beerRepository.save(beer);
    		return "redirect:/beers/" + savedBeer.getId();
    	}
    }

    private PageRequest createPageRequest(int page, int size, Sort.Direction sortDirection, String propertyName) {
        return PageRequest.of(page,
                size,
                Sort.by(sortDirection, propertyName));
    }
}
