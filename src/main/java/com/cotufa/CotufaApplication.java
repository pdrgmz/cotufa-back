package com.cotufa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cotufa.models.Movie;
import com.cotufa.models.User;
import com.cotufa.repository.MovieRepository;
import com.cotufa.repository.UserRepository;

@SpringBootApplication
@EnableJpaRepositories
public class CotufaApplication {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MovieRepository movieRepository;

	public static void main(String[] args) {
		SpringApplication.run(CotufaApplication.class, args);
	}
	
	
	//The first user of the app
	@PostConstruct
	public void initUsers() throws ParseException {
		
		String[] roles = {"GOD", "CREATOR", "USER"};
		
		List<User> users = Stream.of(
				new User(null,"admin", "admin", roles)
				).collect(Collectors.toList());
		userRepository.saveAll(users);	
		
		
		List<Movie> movies = Stream.of(
				new Movie(null, "Free Guy",3, "Action", "Ryan Reynolds, Jodie Comer, Lil Rel Howery",  new SimpleDateFormat("yyy-MM-dd").parse("2021-08-18") ),
				new Movie(null, "The Suicide Squad", 4, "Action", "Margot Robbie, Idris Elba, John Cena, Joel Kinnaman",  new SimpleDateFormat("yyy-MM-dd").parse("2021-08-06") ),
				new Movie(null, "The Tomorrow War", 2, "Science Fiction", "Chris Pratt, Yvonne Strahovski, J.K. Simmons",  new SimpleDateFormat("yyy-MM-dd").parse("2021-07-01") ),
				new Movie(null, "The Boss Baby: Family Business", 2, "Animation", "Alec Baldwin, James Marsden, Amy Sedaris, Jeff Goldblum",  new SimpleDateFormat("yyy-MM-dd").parse("2021-07-02") ),
				new Movie(null, "Reminiscence", 3, "Science Fiction", "Hugh Jackman, Rebecca Ferguson, Thandiwe Newton, Daniel Wu",  new SimpleDateFormat("yyy-MM-dd").parse("2021-08-20") ),
				new Movie(null, "Don't Breathe 2", 4, "Thriller", "Stephen Lang, Brendan Sexton III, Madelyn Grace",  new SimpleDateFormat("yyy-MM-dd").parse("2021-08-13") ),
				new Movie(null, "Snake Eyes: G.I. Joe Origins", 2, "Adventure", "Henry Golding, Andrew Koji, Haruka Abe, Úrsula Corberó",  new SimpleDateFormat("yyy-MM-dd").parse("2021-07-23") ),
				new Movie(null, "Shang-Chi and the Legend of the Ten Rings ", 5, "Action", "Simu Liu, Tony Leung Chiu-wai, Awkwafina, Meng'er Zhang",  new SimpleDateFormat("yyy-MM-dd").parse("2021-09-03") ),
				new Movie(null, "The SpongeBob Movie: Sponge on the Run", 5, "Family", "Tom Kenny, Bill Fagerbakke, Rodger Bumpass, Mr. Lawrence",  new SimpleDateFormat("yyy-MM-dd").parse("2020-08-14") ),
				new Movie(null, "Harry Potter and the Philosopher's Stone ", 5, "Adventure", "Daniel Radcliffe, Rupert Grint, Emma Watson",  new SimpleDateFormat("yyy-MM-dd").parse("2001-11-16") )
				).collect(Collectors.toList());
		movieRepository.saveAll(movies);
		
		
	}
	

}
