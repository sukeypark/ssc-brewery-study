package com.hspark.brewery.security;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.security.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUnlockService {
	
	private final UserRepository userRepository;
	
	@Scheduled(fixedRate = 5000)
	public void unlockAccounts() {
		log.debug("Running Unlock Accounts");
		
		List<User> lockedUsers = userRepository
				.findAllByAccountNonLockedAndLastModifiedDateIsBefore(false, 
						Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));
		
		if (lockedUsers.size() > 0) {
			log.debug("Locked Accounts Found, Unlocking");
			lockedUsers.forEach(user -> user.setAccountNonLocked(true));
			
			userRepository.saveAll(lockedUsers);
		}
	}
}
