package com.hspark.brewery.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hspark.brewery.domain.security.LoginSuccess;

@Repository
public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, Integer> {

}
