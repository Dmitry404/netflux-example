package me.dmitry404.controllers;

import me.dmitry404.domain.Movie;
import me.dmitry404.domain.MovieEvent;
import me.dmitry404.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping("/{id}")
  public Mono<Movie> getMovieById(@PathVariable String id) {
    return movieService.getById(id);
  }

  @GetMapping
  public Flux<Movie> getAllMovies() {
    return movieService.getAllMovies();
  }

  @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<MovieEvent> streamMovieEvents(@PathVariable String id) {
    return movieService.events(id);
  }
}
