package guru.springframework.webfluxstockquoteservice.service;

import guru.springframework.webfluxstockquoteservice.model.Quote;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class QuoteServiceImplTest {

    private QuoteService service = new QuoteServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void fetchQuoteStream() throws InterruptedException {
        Flux<Quote> quoteFlux = this.service.fetchQuoteStream(Duration.ofMillis(100L));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Consumer<Quote> println = System.out::println;
        Consumer<Throwable> errorHandler  = e -> System.err.println("An error ocurred");
        Runnable allDone = () -> countDownLatch.countDown();
        quoteFlux.take(30)
                .subscribe(println, errorHandler, allDone);

        countDownLatch.await();
    }
}