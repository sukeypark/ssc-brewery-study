package com.hspark.brewery.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hspark.brewery.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	List<Customer> findAllByCustomerNameLike(String customerName);

	Optional<Customer> findAllByCustomerName(String customerName);

}
