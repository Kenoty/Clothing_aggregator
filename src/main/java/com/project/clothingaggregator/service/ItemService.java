package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.*;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.ItemMapper;
import com.project.clothingaggregator.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public EbayItemDto createItem(EbayItemDto itemRequest) {
        return ItemMapper.toResponse(itemRepository
                .save(ItemMapper.toEntity(itemRequest)));
    }

    public EbayItemDto getItem(String id) {
        return ItemMapper.toResponse(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    public Page<EbayItemDto> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(ItemMapper::toResponse);
    }

    public EbayItemDto updateItem(String id, EbayItemDto request) {
        EbayClothingItem item = itemRepository.findById(id).orElseThrow(NotFoundException::new);

        return ItemMapper.toResponse(itemRepository
                .save(ItemMapper.updateFromRequest(request, item)));
    }

    public void deleteItem(String id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item with id = " + id + " not found");
        }
        itemRepository.deleteById(id);
    }

}
