package com.dpk.Enotes.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dpk.Enotes.model.User;

//CustomUserDetails ==> CustomUser matra lekda ni class
public class CustomUserDetails implements UserDetails {

	private User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {

		return user.getPassword();
	}

	@Override
	public String getUsername() {

		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;		
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return user.isEnable();
	}

	// Parameterized Constructor banaune
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	// getter and setter methods banaune
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
