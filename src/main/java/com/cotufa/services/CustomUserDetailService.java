package com.cotufa.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.cotufa.models.CustomUserDetails;
import com.cotufa.models.User;
import com.cotufa.repository.UserRepository;

@Service
public class CustomUserDetailService  implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		
		Optional<User> user = userRepository.findByUsername(username);		
		if( user.isPresent() ) {
			
			return new CustomUserDetails(user.get()) ;			
		}
		throw new UsernameNotFoundException("User not found");
				
	}

}
