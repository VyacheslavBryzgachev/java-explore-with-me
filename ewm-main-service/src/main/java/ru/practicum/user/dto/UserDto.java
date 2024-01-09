package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    @NotNull
    @Length(min = 2, max = 250)
    private String name;
    @Email
    @NotNull
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;
}
