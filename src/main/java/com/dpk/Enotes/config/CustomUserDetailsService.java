package com.dpk.Enotes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dpk.Enotes.model.User;
import com.dpk.Enotes.repository.UserRepository;

//@Component
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepo.findByEmail(email);
		
		if(user == null) {
			
			throw new UsernameNotFoundException("user not found..!!");
		}
		else {
			
			return new CustomUserDetails(user);
		}
		
	}

}
