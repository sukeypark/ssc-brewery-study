package com.hspark.brewery.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.BeerOrder;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID>{

}
