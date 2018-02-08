package me.dmitry404.services;

import me.dmitry404.domain.Quote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Service
public class QuoteGeneratedServiceImpl implements QuoteGeneratedService {
  private final MathContext mathContext = new MathContext(2);
  private final Random random = new Random();

  private final List<Quote> prices = new ArrayList<>();

  public QuoteGeneratedServiceImpl() {
    prices.add(new Quote("AAPL", 159.54));
    prices.add(new Quote("GOOG", 1048.58));
    prices.add(new Quote("FB", 180.18));
    prices.add(new Quote("ORCL", 48.86));
    prices.add(new Quote("MSFT", 89.61));
    prices.add(new Quote("RHT", 128.79));
  }

  @Override
  public Flux<Quote> fetchQuoteStream(Duration period) {
    return Flux.generate(
        () -> 0,
        (BiFunction<Integer, SynchronousSink<Quote>, Integer>) (index, sink) -> {
          Quote updatedQuote = updateQuote(prices.get(index));
          sink.next(updatedQuote);
          return ++index % prices.size();
        })
        .zipWith(Flux.interval(period))
        .map(Tuple2::getT1)
        .map(quote -> {
          quote.setInstant(Instant.now());
          return quote;
        })
        .log("QuoteGenerator");
  }

  private Quote updateQuote(Quote quote) {
    BigDecimal priceChange = quote.getPrice()
        .multiply(new BigDecimal((random.nextBoolean() ? 1 : -1) * 0.05 * random.nextDouble(), mathContext));
    return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
  }
}
