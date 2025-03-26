package ru.practicum;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;


@Slf4j
@Component
public class StatWebClient {
    private final WebClient webClient;


    public StatWebClient(WebClient.Builder webClientBuilder, @Value("${stats.client.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public HitDto addHit(HitDto hitDto) {
        log.info("Метод hit --> ");
        return
        webClient.post()
                .uri("/hit")
                .bodyValue(hitDto)
                .retrieve()
                .bodyToMono(HitDto.class)
                .block();
    }

       public Mono<StatDto> get(StatRequestDto statRequestDto) {
           log.info("Метод get --> ");
         UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/stats");
         uriBuilder.queryParam("start", statRequestDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
         uriBuilder.queryParam("end", statRequestDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
         if (statRequestDto.getUri() != null) {
             for (String uri : statRequestDto.getUri()) {
                 uriBuilder.queryParam("uris", uri);
             }
         }
         uriBuilder.queryParam("unique", statRequestDto.getUnique());
         return webClient
                 .get()
                 .uri(uriBuilder.build().toUri())
                 .retrieve()
                 .bodyToMono(StatDto.class);

     }

    public Long getEventViews(String request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/stats/event");
        uriBuilder.queryParam("uri", request);
        log.info("getEventViews uri --> {}", uriBuilder.toUriString());
        return webClient
                .get()
                .uri(uriBuilder.build().toUri())
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

}
