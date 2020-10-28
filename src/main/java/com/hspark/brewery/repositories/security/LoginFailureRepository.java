package com.hspark.brewery.repositories.security;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.security.LoginFailure;
import com.hspark.brewery.domain.security.User;

@Repository
public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer> {
	
	List<LoginFailure> findAllByUserAndCreatedDateIsAfter(User user, Timestamp timestamp);
	
}
