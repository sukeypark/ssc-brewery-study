package com.hspark.brewery.repositories.security;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	
	List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean nonLocked, Timestamp timestamp);
}
