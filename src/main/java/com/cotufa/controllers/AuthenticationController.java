package com.cotufa.controllers;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cotufa.models.AuthenticationResponse;
import com.cotufa.models.GenericResponse;
import com.cotufa.models.User;
import com.cotufa.models.UserRequest;
import com.cotufa.repository.UserRepository;
import com.cotufa.util.JwtUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Authentication", description = "Manage everything related to the authentication of the application")
@Controller
@RequestMapping("")
@CrossOrigin
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	UserRepository userRepository;
	
	@ApiOperation(value = "Generate a JWT given a valid username and password")
	@RequestMapping(method = RequestMethod.POST, path = "/token")
	public ResponseEntity<?> createAuthToken(@RequestBody UserRequest user) throws Exception{
		
		try {			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			
		} catch (Exception e) {
			return new ResponseEntity(new GenericResponse("Your login and password do not match"), HttpStatus.BAD_REQUEST);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
		
		final String jwt = jwtUtils.generateToken(userDetails);	
		
		return new ResponseEntity(new AuthenticationResponse(jwt), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Create a new user ")
	@RequestMapping(method = RequestMethod.POST, path = "/user")
	public ResponseEntity<?> createNewUser(@RequestBody UserRequest user) throws Exception{
		
		if( user.getUsername() == null) {
			return new ResponseEntity(new GenericResponse("Username missing"), HttpStatus.BAD_REQUEST);
		}
		if( user.getPassword() == null) {
			return new ResponseEntity(new GenericResponse("Password missing"), HttpStatus.BAD_REQUEST);
		}
		
		User newUser = new User(null, user.getUsername(), user.getPassword(), "CREATOR");
		User created = null;
		try {
			created = userRepository.save(newUser);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if( created!= null && created.getId()!= null) {
			return new ResponseEntity(new GenericResponse("User successfully created"), HttpStatus.CREATED);
		}else {
			return new ResponseEntity(new GenericResponse("The user could not be created"), HttpStatus.BAD_REQUEST);
		}

		
	}

}
