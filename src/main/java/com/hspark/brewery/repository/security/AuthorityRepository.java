package com.hspark.brewery.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.security.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
