package com.project.clothingaggregator.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateRequest {
    @Pattern(regexp = "^[\\p{L}'-]{2,50}$", message = "incorrect name")
    private String username;

    @NotNull(message = "date must be in not null")
    @Past(message = "date must be in the past")
    private LocalDate birthday;

    @NotBlank(message = "email must be not blank")
    @Email(message = "incorrect email")
    private String email;
}
