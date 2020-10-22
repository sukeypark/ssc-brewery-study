package com.hspark.brewery.bootstrap;

import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.domain.BeerInventory;
import com.hspark.brewery.domain.BeerOrder;
import com.hspark.brewery.domain.BeerOrderLine;
import com.hspark.brewery.domain.Brewery;
import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.domain.OrderStatusEnum;
import com.hspark.brewery.repositories.BeerInventoryRepository;
import com.hspark.brewery.repositories.BeerOrderRepository;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.repositories.BreweryRepository;
import com.hspark.brewery.repositories.CustomerRepository;
import com.hspark.brewery.web.model.BeerStyleEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {
	
    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;

	@Override
	public void run(String... args) throws Exception {
		loadBrewerData();
		loadCustomerData();
	}

	private void loadCustomerData() {
		Customer tastingRoom = Customer.builder()
				.customerName(TASTING_ROOM)
				.apiKey(UUID.randomUUID())
				.build();
		
		customerRepository.save(tastingRoom);
		
		beerRepository.findAll().forEach(beer -> {
			beerOrderRepository.save(BeerOrder.builder()
					.customer(tastingRoom)
					.orderStatus(OrderStatusEnum.NEW)
					.beerOrderLines(Set.of(BeerOrderLine.builder()
							.beer(beer)
							.orderQuantity(2)
							.build()))
					.build());
		});
	}

	private void loadBrewerData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
	}
	
	

}
