package com.project.clothingaggregator.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private LocalDate birthday;
    private String email;
}
