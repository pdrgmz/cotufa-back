package com.cotufa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotufa.models.GenericResponse;
import com.cotufa.models.Movie;
import com.cotufa.services.MovieService;
import com.cotufa.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Movies", description = "Manage everything related to the movies")
@Controller
@RequestMapping("")
@CrossOrigin
public class MoviesController {
	
	
	@Autowired
	MovieService movieService;	
	
	@ApiOperation(value = "Get all movies")
	@RequestMapping(method = RequestMethod.GET, path = "/movies")
	private ResponseEntity<?> getAllMovies(			
		
			@RequestParam(required = false) String genre, // Filter by genre
			@RequestParam(required = false) String title, // Filter by genre

			//Pagination
			@RequestParam(required = false, defaultValue = "0") Integer page,
    		@RequestParam(required = false, defaultValue = "9") Integer size,
    		@RequestParam(required = false) String[] sort
    		) throws Exception{
		
		//Sorting by a field or default by title
		String[] order = sort != null ?  sort : new String[]{"title"};
				
		
		Movie search = new Movie();
		
		//Set genre to filter
		if(genre != null) {
			search.setGenre(genre);
		}
		
		//Set title to search
		if(title != null) {
			search.setTitle(title);
		}

		
		Page<Movie> response = movieService.getAll(search, page, size, order);  
		
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get all genres from all movies")
	@RequestMapping(method = RequestMethod.GET, path = "/genres")
	private ResponseEntity<?> getAllGenres(	){		
		return new ResponseEntity(movieService.getGrouped(), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Get movie details")
	@RequestMapping(method = RequestMethod.GET, path = "/movies/{id}")
	private ResponseEntity getMovie(@PathVariable("id") Integer id) {  
		
		Movie movie = movieService.getById(id);
		if(movie != null) {
			return new ResponseEntity(movie, HttpStatus.OK);
		}		
		return new ResponseEntity(new GenericResponse("Movie not found"), HttpStatus.NOT_FOUND);
		
	}
	
	@ApiOperation(value = "Modify details of a movie")
	@RequestMapping(method = RequestMethod.PATCH, path = "/movies/{id}")
	private ResponseEntity patchMovie(@PathVariable("id") Integer id, @RequestBody Movie newMovie) {
		
		
		//Find the stored movie
		Movie oldMovie = movieService.getById(id); 
		
		if( oldMovie == null ) {
			return new ResponseEntity(new GenericResponse("Movie not found"), HttpStatus.NOT_FOUND);
		}		
		newMovie.setId(id);
		
		//Modify only the fields on the request
	    Util.merge(newMovie, oldMovie);
	    
		movieService.saveOrUpdate(oldMovie);	
		
		return new ResponseEntity(oldMovie, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete a movie")
	@RequestMapping(method = RequestMethod.DELETE, path = "/movies/{id}")
	private ResponseEntity deleteMovie(@PathVariable("id") Integer id) {  
		if( !movieService.delete(id) ) {
			return new ResponseEntity(new GenericResponse("The movie could not be deleted"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(new GenericResponse("The movie has been deleted"), HttpStatus.OK);
	} 
	
	@ApiOperation(value = "Create a movie with its details")
	@RequestMapping(method = RequestMethod.POST, path = "/movies")
	private ResponseEntity saveMovie(@RequestBody Movie movie) {		
		
		if(movie.getTitle() == null) {
			return new ResponseEntity(new GenericResponse("Movie title missing"), HttpStatus.BAD_REQUEST);
		}
		if(movie.getRating() == null) {
			return new ResponseEntity(new GenericResponse("Movie rating missing"), HttpStatus.BAD_REQUEST);
		}
		if(movie.getGenre() == null) {
			return new ResponseEntity(new GenericResponse("Movie genre missing"), HttpStatus.BAD_REQUEST);
		}
		if(movie.getCastCrew() == null) {
			return new ResponseEntity(new GenericResponse("Movie castCrew missing"), HttpStatus.BAD_REQUEST);
		}
		if(movie.getReleaseDate() == null) {
			return new ResponseEntity(new GenericResponse("Movie release date missing"), HttpStatus.BAD_REQUEST);
		}
		
		
		if(0 > movie.getRating() || movie.getRating() > 100) {
			return new ResponseEntity(new GenericResponse("The rating must be between 0 and 100"), HttpStatus.BAD_REQUEST);
		}
			
		return new ResponseEntity(movieService.saveOrUpdate(movie), HttpStatus.CREATED);
	}
	


}
