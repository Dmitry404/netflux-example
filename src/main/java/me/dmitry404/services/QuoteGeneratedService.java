package me.dmitry404.services;

import me.dmitry404.domain.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

public interface QuoteGeneratedService {
  Flux<Quote> fetchQuoteStream(Duration period);
}
