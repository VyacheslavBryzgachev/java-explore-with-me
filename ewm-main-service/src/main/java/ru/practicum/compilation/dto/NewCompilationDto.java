package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class NewCompilationDto {
    private Set<Long> events;
    private Boolean pinned;
    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
