package com.hspark.brewery.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.BeerOrder;
import com.hspark.brewery.domain.Customer;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID>{

	Page<BeerOrder> findAllByCustomer(Customer customer, Pageable pageable);
	
	@Query("select o from BeerOrder o where o.id = ?1 and "
			+ "(true = :#{hasAuthority('order.read')} or "
			+ "o.customer.id =?#{principal?.customer?.id})")
	BeerOrder findOrderByIdSecure(UUID orderId);

}
