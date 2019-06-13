package guru.springframework.webfluxstockquoteservice.service;

import guru.springframework.webfluxstockquoteservice.model.Quote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final List<Quote> prices = new ArrayList<>();
    private final Random random = new Random();
    private final MathContext mathContext = new MathContext(2);

    public QuoteServiceImpl() {
        this.prices.add(new Quote("AAPL", 160.16));
        this.prices.add(new Quote("MSF", 77.74));
        this.prices.add(new Quote("GOOG", 847.24));
        this.prices.add(new Quote("ORCL", 49.51));
        this.prices.add(new Quote("IBM", 159.34));
        this.prices.add(new Quote("INTC", 39.29));
        this.prices.add(new Quote("RHT", 84.24));
        this.prices.add(new Quote("VMW", 92.21));
    }


    @Override
    public Flux<Quote> fetchQuoteStream(Duration duration) {
        return Flux.generate(()->0,
                (BiFunction<Integer, SynchronousSink<Quote>, Integer>) (index, sink) ->{
                    Quote updatedQuote = updatePrice(this.prices.get(index));
                    sink.next(updatedQuote);
                    return ++index%this.prices.size();
                })
                .zipWith(Flux.interval(duration))
                .map(t -> t.getT1())
                .map(quote -> {
                    quote.setInstant(Instant.now());
                    return quote;
                })
                .log("guru.springframework.webfluxstockquoteservice.service.QuoteService");
    }

    private Quote updatePrice(Quote quote) {
        BigDecimal newChange = quote.getPrice()
                .multiply(new BigDecimal(0.05*this.random.nextDouble()));
        return new Quote(quote.getTicker(), quote.getPrice().add(newChange));
    }
}
