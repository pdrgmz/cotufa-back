package com.cotufa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotufa.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	Optional<User> findByUsername(String username);	

}
