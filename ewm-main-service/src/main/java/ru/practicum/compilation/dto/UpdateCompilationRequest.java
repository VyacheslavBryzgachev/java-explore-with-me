package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@Builder
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
