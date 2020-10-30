package com.hspark.brewery.security.google;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hspark.brewery.domain.security.User;
import com.hspark.brewery.repositories.security.UserRepository;
import com.warrenstrange.googleauth.ICredentialRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class GoogleCredentialRepository implements ICredentialRepository{
	
	private final UserRepository userRepository;

	@Override
	public String getSecretKey(String userName) {
		User user = userRepository.findByUsername(userName).orElseThrow();
		return user.getGoogle2FaSecret();
	}

	@Override
	public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
		User user = userRepository.findByUsername(userName).orElseThrow();
		user.setGoogle2FaSecret(secretKey);
		user.setUseGoogle2fa(true);
		userRepository.save(user);
	}

}
