package me.dmitry404.netfluxexample;

import me.dmitry404.domain.Quote;
import me.dmitry404.services.QuoteGeneratedService;
import me.dmitry404.services.QuoteGeneratedServiceImpl;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class QuoteGeneratorServiceTest {
  private QuoteGeneratedService quoteGeneratedService = new QuoteGeneratedServiceImpl();

  @Test
  public void testFetchQuoteStream() throws Exception {
    Flux<Quote> quoteFlux = quoteGeneratedService.fetchQuoteStream(Duration.ofMillis(1000L));

    quoteFlux.take(10).subscribe(System.out::println);
    // here will be no output as the test far before quoteFlux pushes 10 quotes
  }

  @Test
  public void testFetchQuoteStream_withResults() throws Exception {
    Flux<Quote> quoteFlux = quoteGeneratedService.fetchQuoteStream(Duration.ofMillis(1000L));

    CountDownLatch countDownLatch = new CountDownLatch(1);

    Consumer<Quote> onEach = System.out::println;
    Consumer<Throwable> onError = System.err::println;
    Runnable onDone = countDownLatch::countDown;

    quoteFlux.take(10).subscribe(onEach, onError, onDone);

    countDownLatch.await();
  }
}
