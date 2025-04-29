package com.project.clothingaggregator.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserWithFavoritesDto {
    private Integer userId;
    private String username;
    private String email;
    private LocalDate birthday;
    private List<EbayItemDto> favorites;
}
