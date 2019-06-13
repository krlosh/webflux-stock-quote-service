package guru.springframework.webfluxstockquoteservice.web;

import guru.springframework.webfluxstockquoteservice.model.Quote;
import guru.springframework.webfluxstockquoteservice.service.QuoteService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class QuoteHandler {

    private final QuoteService quoteService;

    public QuoteHandler(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    public Mono<ServerResponse> fetchQuotes(ServerRequest request){
        int size = Integer.parseInt(request.queryParam("size").orElse(10));

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.quoteService.fetchQuoteStream(Duration.ofMillis(100))
                            .take(size), Quote.class);
    }
}
