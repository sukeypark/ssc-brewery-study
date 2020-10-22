package com.hspark.brewery.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	List<Customer> findAllByCustomerNameLike(String string);

}
