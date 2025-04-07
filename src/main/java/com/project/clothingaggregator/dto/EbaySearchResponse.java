package com.project.clothingaggregator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EbaySearchResponse {
    private ItemSummary[] itemSummaries;
}
