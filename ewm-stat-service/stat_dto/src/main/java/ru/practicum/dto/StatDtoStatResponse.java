package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatDtoStatResponse {
    private String app;
    private String uri;
    private int hits;

}
