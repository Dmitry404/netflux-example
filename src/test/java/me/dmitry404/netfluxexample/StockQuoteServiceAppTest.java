package me.dmitry404.netfluxexample;

import me.dmitry404.domain.Quote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockQuoteServiceAppTest {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void fetchQuotesTest() throws Exception {
    webTestClient
        .get()
        .uri("/quotes?size=5")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Quote.class)
        .hasSize(5)
        .consumeWith(
            quotes -> {
              assertThat(quotes.getResponseBody())
                  .allSatisfy(quote -> assertThat(quote.getPrice()).isPositive());
              assertThat(quotes.getResponseBody()).hasSize(5);
            }
        );
  }

  @Test
  public void streamQuotesTest() throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(10);

    webTestClient
        .get()
        .uri("/quotes")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .returnResult(Quote.class)
        .getResponseBody()
        .take(10)
        .subscribe(quote -> {
          assertThat(quote.getPrice()).isPositive();

          countDownLatch.countDown();
        });

    countDownLatch.await();
  }
}
