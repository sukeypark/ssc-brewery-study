package com.hspark.brewery.bootstrap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.Beer;
import com.hspark.brewery.domain.BeerInventory;
import com.hspark.brewery.domain.BeerOrder;
import com.hspark.brewery.domain.BeerOrderLine;
import com.hspark.brewery.domain.Brewery;
import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.domain.OrderStatusEnum;
import com.hspark.brewery.domain.security.Authority;
import com.hspark.brewery.domain.security.Role;
import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.BeerInventoryRepository;
import com.hspark.brewery.repositories.BeerOrderRepository;
import com.hspark.brewery.repositories.BeerRepository;
import com.hspark.brewery.repositories.BreweryRepository;
import com.hspark.brewery.repositories.CustomerRepository;
import com.hspark.brewery.repositories.security.AuthorityRepository;
import com.hspark.brewery.repositories.security.RoleRepository;
import com.hspark.brewery.repositories.security.UserRepository;
import com.hspark.brewery.web.model.BeerStyleEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {
	
    public static final String TASTING_ROOM = "Tasting Room";
    public static final String ST_PETE_DISTRIBUTING = "St Pete Distributing";
    public static final String DUNEDIN_DISTRIBUTING = "Dunedin Distributing";
    public static final String KEY_WEST_DISTRIBUTORS = "Key West Distributors";
    public static final String STPETE_USER = "stpete";
    public static final String DUNEDIN_USER = "dunedin";
    public static final String KEYWEST_USER = "keywest";
    
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		loadSecurityData();
		loadBreweryData();
		loadTastingRoomData();
		loadCustomerData();
	}

	private void loadCustomerData() {
		Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();

		Customer stPeteCustomer = customerRepository.save(Customer.builder()
				.customerName(ST_PETE_DISTRIBUTING)
				.apiKey(UUID.randomUUID())
				.build());
		
		Customer dunedinCustomer = customerRepository.save(Customer.builder()
				.customerName(DUNEDIN_DISTRIBUTING)
				.apiKey(UUID.randomUUID())
				.build());
		
		Customer keyWestCustomer = customerRepository.save(Customer.builder()
				.customerName(KEY_WEST_DISTRIBUTORS)
				.apiKey(UUID.randomUUID())
				.build());
		
        User stPeteUser = userRepository.save(User.builder().username(STPETE_USER)
                .password(passwordEncoder.encode("password"))
                .customer(stPeteCustomer)
                .role(customerRole).build());

        User dunedinUser = userRepository.save(User.builder().username(DUNEDIN_USER)
                .password(passwordEncoder.encode("password"))
                .customer(dunedinCustomer)
                .role(customerRole).build());

        User keywest = userRepository.save(User.builder().username(KEYWEST_USER)
                .password(passwordEncoder.encode("password"))
                .customer(keyWestCustomer)
                .role(customerRole).build());
        
        createOrder(stPeteCustomer);
        createOrder(dunedinCustomer);
        createOrder(keyWestCustomer);
        
        log.debug("Orders Loaded: " + beerOrderRepository.count());
	}

	private BeerOrder createOrder(Customer customer) {
        return  beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2)
                        .build()))
                .build());
	}

	private void loadTastingRoomData() {
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

	private void loadBreweryData() {
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
	
	private void loadSecurityData() {
		
		// beer auths
		Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
		Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
		Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
		Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());
		
		// customer auths
		Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
		Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
		Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
		Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());
		
		// brewery auths
		Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
		Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
		Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
		Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());
		
		// beer order
		Authority createOrder = authorityRepository.save(Authority.builder().permission("order.create").build());
		Authority updateOrder = authorityRepository.save(Authority.builder().permission("order.update").build());
		Authority readOrder = authorityRepository.save(Authority.builder().permission("order.read").build());
		Authority deleteOrder = authorityRepository.save(Authority.builder().permission("order.delete").build());
		
		// customer beer order
		Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.create").build());
		Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.update").build());
		Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.read").build());
		Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.delete").build());
		
		
		Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
		Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
		Role userRole = roleRepository.save(Role.builder().name("USER").build());
		
		adminRole.setAuthorities(new HashSet<>(Set.of(
				createBeer, updateBeer, readBeer, deleteBeer,
				createCustomer, updateCustomer, readCustomer, deleteCustomer,
				createBrewery, updateBrewery, readBrewery, deleteBrewery,
				createOrder, updateOrder, readOrder, deleteOrder)));
		
		customerRole.setAuthorities(new HashSet<>(Set.of(
				readBeer, readCustomer, readBrewery,
				createOrderCustomer, updateOrderCustomer, readOrderCustomer, deleteOrderCustomer)));
		 
		userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

		roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));
		
		userRepository.save(
				User.builder()
				.username("admin")
				.password(passwordEncoder.encode("password"))
				.role(adminRole)
				.build()					
				);
		
		userRepository.save(
				User.builder()
				.username("customer")
				.password(passwordEncoder.encode("password"))
				.role(customerRole)
				.build()
				);
		
		userRepository.save(
				User.builder()
				.username("user")
				.password(passwordEncoder.encode("password"))
				.role(userRole)
				.build()	
				
				);
		
		log.debug("Users Loaded: " + userRepository.count());

	}	

}
