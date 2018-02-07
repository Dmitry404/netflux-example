package me.dmitry404.repositories;

import me.dmitry404.domain.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
}
