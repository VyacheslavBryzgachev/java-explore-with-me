package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.dto.StatDtoStatResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class StatsClient {

    private final WebClient webClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${stats.server.url}") String statsServerUrl) {
        webClient = WebClient.create(statsServerUrl);
    }

    public void createStat(StatDtoRequest statsDto) {
        webClient.post().uri("/hit")
                .bodyValue(statsDto)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public ResponseEntity<List<StatDtoStatResponse>> getStats(LocalDateTime start, LocalDateTime end, @Nullable List<String> uris,
                                                              Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/stats")
                            .queryParam("start", start.format(formatter))
                            .queryParam("end", end.format(formatter));
                    if (uris != null)
                        uriBuilder.queryParam("uris", String.join(",", uris));
                    if (unique != null)
                        uriBuilder.queryParam("unique", unique);
                    return uriBuilder.build();
                })
                .retrieve()
                .toEntityList(StatDtoStatResponse.class)
                .block();
    }

    public ResponseEntity<Long> getView(Long eventId) {
        return webClient.get().uri("/stats/view/" + eventId)
                .retrieve()
                .toEntity(Long.class)
                .block();
    }
}