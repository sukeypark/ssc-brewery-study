package com.hspark.brewery;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

class PasswordEncodingTests {
	
	static final String PASSWORD = "password";

	@Test
	void testMd5Password() {
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		
		String salted = PASSWORD + "salt";
		System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
	}
	
	@Test
	void testNoOpPassword() {
		PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
		System.out.println(noOp.encode(PASSWORD));
		System.out.println(PASSWORD);
	}
	
	// older encryption method. usually used in legacy system
	// use random salt by default
	@Test
	void testLdapPassword() {
		PasswordEncoder ldap = new LdapShaPasswordEncoder();
		System.out.println(ldap.encode(PASSWORD));
		System.out.println(ldap.encode(PASSWORD));
		
		String encodedPwd = ldap.encode(PASSWORD);
		
		assertTrue(ldap.matches(PASSWORD, encodedPwd));
	}
	
	// old standard password encoder for spring framework
	// use random salt by default
	@Test
	void testSha256Password() {
		PasswordEncoder sha256 = new StandardPasswordEncoder();
		System.out.println(sha256.encode(PASSWORD));
		System.out.println(sha256.encode(PASSWORD));
	}
	
	// default password encoder for spring security framework
	// strength(default) = 10
	@Test
	void testBcryptPassword() {
		PasswordEncoder bcrypt = new BCryptPasswordEncoder();
		System.out.println(bcrypt.encode(PASSWORD));
		System.out.println(bcrypt.encode(PASSWORD));
	}
	
	

}
