package com.hspark.brewery.domain.security;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hspark.brewery.domain.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String username;
	private String password;
	
	// fetch EAGER: avoid N+1 SQL problem in ManyToMany Relationship
	// get 1 SQL query rather than multiple query
	@Singular(value = "role")
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
		joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
		inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
	private Set<Role> roles;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Customer customer;
	
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(Role::getAuthorities)
				.flatMap(Set::stream)
				.map(authority -> {
					return new SimpleGrantedAuthority(authority.getPermission()); 
				})
				.collect(Collectors.toSet());
	}
	
	@Builder.Default
	private boolean accountNonExpired = true;

	@Builder.Default
	private boolean accountNonLocked = true;
	
	@Builder.Default
	private boolean credentialsNonExpired = true;
	
	@Builder.Default
	private boolean enabled = true;

	@Override
	public void eraseCredentials() {
		this.password = null;
	}
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp lastModifiedDate;
}
