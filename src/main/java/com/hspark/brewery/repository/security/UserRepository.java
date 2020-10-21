package com.hspark.brewery.repository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.security.Authority;
import com.hspark.brewery.domain.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
}
