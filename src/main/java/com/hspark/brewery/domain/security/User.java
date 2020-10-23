package com.hspark.brewery.domain.security;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

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
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String username;
	private String password;
	
	// fetch EAGER: avoid N+1 SQL problem in ManyToMany Relationship
	// get 1 SQL query rather than multiple query
	@Singular(value = "role")
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
		joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
		inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
	private Set<Role> roles;
	
	// tells Hibernate or JPA that this property is calculated and it is not persistent
	@Transient
	private Set<Authority> authorities;
	
	public Set<Authority> getAuthorities() {
		return this.roles.stream()
				.map(Role::getAuthorities)
				.flatMap(Set::stream)
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
}
