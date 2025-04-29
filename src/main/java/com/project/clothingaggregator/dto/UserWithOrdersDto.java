package com.project.clothingaggregator.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class UserWithOrdersDto {
    private Integer userId;
    private String username;
    private String email;
    private LocalDate birthday;
    private List<OrderWithItemsDto> orders;
}
