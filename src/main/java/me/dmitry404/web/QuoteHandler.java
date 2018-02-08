package me.dmitry404.web;

import me.dmitry404.domain.Quote;
import me.dmitry404.services.QuoteGeneratedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class QuoteHandler {
  private final QuoteGeneratedService quoteGeneratedService;

  @Autowired
  public QuoteHandler(QuoteGeneratedService quoteGeneratedService) {
    this.quoteGeneratedService = quoteGeneratedService;
  }

  public Mono<ServerResponse> fetchQuotes(ServerRequest request) {
    int size = Integer.parseInt(request.queryParam("size").orElse("10"));
    return ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(quoteGeneratedService.fetchQuoteStream(Duration.ofMillis(100L)).take(size), Quote.class);
  }

  public Mono<ServerResponse> streamQuotes(ServerRequest request) {
    return ok()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .body(quoteGeneratedService.fetchQuoteStream(Duration.ofMillis(500L)), Quote.class);

  }
}
