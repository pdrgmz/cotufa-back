package com.cotufa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.cotufa.models.Movie;
import com.cotufa.repository.MovieRepository;

@Service
public class MovieService {
	
	@Autowired  
	MovieRepository movieRepository;  
	
	public Page<Movie> getAll( Movie movie, int page, int pageSize, String[] order){  
		
		
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			      .withMatcher("genre", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Movie> example = Example.of(movie, customExampleMatcher);	
		
		
		page = page < 0 ? 0 : page;//Page cannot be less than 0
		pageSize = pageSize < 1 ? 1 : pageSize;//PageSize cannot be less than 1

		
		try {
			return movieRepository.findAll( example, PageRequest.of(page, pageSize, Sort.by( Sort.Order.asc( order[0] ).ignoreCase() ) ) );
		} catch (Exception e) {
			return movieRepository.findAll( example, PageRequest.of(page, pageSize ) ) ;
		}	
		
	} 	
	
	public String[] getGrouped(){
		return movieRepository.findByGenreGrouped();
	}	
	
	public Movie getById(Integer id){  
		
		if(movieRepository.findById(id).isPresent()) {
			return movieRepository.findById(id).get();  
		}
		return null;
		
	}  
	
	
	public Movie saveOrUpdate(Movie movie){  
		return movieRepository.save(movie);  
	}
	public void update(Integer id, Movie movie){  
		movieRepository.save(movie);  	
	}
	
	
	public boolean delete(int id){  
		try {
			movieRepository.deleteById(id);  
			return true;
		} catch (Exception e) {
			return false;
		}
		
	} 

}
