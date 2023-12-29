package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatDtoStatResponse {
    private String app;
    private String uri;
    private long hits;

    public StatDtoStatResponse(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
