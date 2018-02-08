package me.dmitry404.services;

import me.dmitry404.domain.Movie;
import me.dmitry404.domain.MovieEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
  Flux<Movie> getAllMovies();

  Mono<Movie> getById(String movieId);

  Flux<MovieEvent> events(String movieId);
}
