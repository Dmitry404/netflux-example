package me.dmitry404.services;

import me.dmitry404.domain.Movie;
import me.dmitry404.domain.MovieEvent;
import me.dmitry404.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@Service
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;

  @Autowired
  public MovieServiceImpl(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @Override
  public Flux<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  @Override
  public Mono<Movie> getById(String movieId) {
    return movieRepository.findById(movieId);
  }

  @Override
  public Flux<MovieEvent> events(String movieId) {
    return Flux.<MovieEvent>generate(synchronousSink -> synchronousSink.next(new MovieEvent(movieId, new Date())))
        .delayElements(Duration.ofSeconds(1));
  }
}
