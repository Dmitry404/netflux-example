package me.dmitry404.netfluxexample;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReactiveStreamsExamples {
  @Test
  public void fluxToStream() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names.toStream().forEach(System.out::println);
    //names.toIterable().forEach(System.out::println);
  }

  @Test
  public void flux_sunscribe() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names.subscribe(System.out::println);
  }

  @Test
  public void flux_doOnEach_withNoSubscribe() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names.doOnEach(elem -> System.out.println(elem.get()));
  }

  @Test
  public void flux_doOnEach_withSubscribe() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names.doOnEach(elem -> System.out.println(elem.get())).subscribe();
    // output contains null at the end (because of no "done" handler
  }

  @Test
  public void flux_subscribe_withParams() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    Consumer<String> onEach = System.out::println;
    Consumer<Throwable> onError = System.err::println;
    Runnable onDone = () -> System.out.println("Done");

    names.subscribe(onEach, onError, onDone);
    // no null at the output's end
  }

  @Test
  public void flux_anotherDoOnEach() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .map(String::length)
        .doOnEach(System.out::println)
        .subscribe();
  }

  @Test
  public void flux_map() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .map(String::length)
        .subscribe(System.out::println);
  }

  @Test
  public void flux_filter() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .filter(s -> s.contains("J"))
        .subscribe(System.out::println);
  }

  @Test
  public void flux_filter_and_takeTwo() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .filter(s -> s.contains("J"))
        .take(2)
        .subscribe(System.out::println);
  }

  @Test
  public void flux_sort_and_takeTwo() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .sort()
        .take(2)
        .subscribe(System.out::println);
  }

  @Test
  public void flux_sort_and_joining_toMono() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .sort()
        .collect(Collectors.joining(", "))
        .subscribe(System.out::println);
  }

  @Test
  public void flux_reductionToMono() throws Exception {
    Flux<String> names = Flux.just("John", "Jane", "Joan", "Bob", "Alice");

    names
        .reduce((a, b) -> a + ", " + b)
        .subscribe(System.out::println);
  }

  @Test
  public void flux_flatMap() throws Exception {
    Flux<List<String>> names = Flux.just(
        Arrays.asList("Bob", "Alice"),
        Arrays.asList("John", "Jane", "Joan")
    );

    names
        .flatMap(Flux::fromIterable)
        .subscribe(System.out::println);
  }

  @Test
  public void flux_listOfLists_flatMap_1() throws Exception {
    Flux<List<List<String>>> names = Flux.just(
        Arrays.asList(
            Arrays.asList("Bob", "Alice"),
            Arrays.asList("John", "Jane", "Joan")
        ),
        Arrays.asList(
            Arrays.asList("Homer", "Bart")
        )
    );

    names
        .flatMap(Flux::fromIterable)
        .subscribe(System.out::println);

    //[Bob, Alice]
    //[John, Jane, Joan]
    //[Homer, Bart]
  }

  @Test
  public void flux_listOfLists_flatMap_2() throws Exception {
    Flux<List<List<String>>> names = Flux.just(
        Arrays.asList(
            Arrays.asList("Bob", "Alice"),
            Arrays.asList("John", "Jane", "Joan")
        ),
        Arrays.asList(
            Arrays.asList("Homer", "Bart")
        )
    );

    names
        .flatMap(Flux::fromIterable)
        .flatMap(Flux::fromIterable)
        .subscribe(System.out::println);

    //Bob
    //Alice
    //John
    //Jane
    //Joan
    //Homer
    //Bart
  }

  @Test
  public void flux_listOfLists_flatMap_sameAs2() throws Exception {
    Flux<List<List<String>>> names = Flux.just(
        Arrays.asList(
            Arrays.asList("Bob", "Alice"),
            Arrays.asList("John", "Jane", "Joan")
        ),
        Arrays.asList(
            Arrays.asList("Homer", "Bart")
        )
    );

    names
        .flatMap(lists -> Flux.fromIterable(
            lists.stream().flatMap(Collection::stream).collect(Collectors.toList()))
        )
        .subscribe(System.out::println);
  }
}
