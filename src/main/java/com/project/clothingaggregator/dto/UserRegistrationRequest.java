package com.project.clothingaggregator.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegistrationRequest {
    private String username;
    private LocalDate birthday;
    private String email;
    private String password;

}
