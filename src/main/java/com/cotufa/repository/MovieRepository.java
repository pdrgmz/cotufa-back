package com.cotufa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotufa.models.Movie;



public interface MovieRepository extends JpaRepository<Movie, Integer>{
	
	@Query("select u.genre from Movie u group by u.genre")
	String[] findByGenreGrouped();

}
