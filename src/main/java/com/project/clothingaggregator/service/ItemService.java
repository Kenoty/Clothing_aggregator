package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.*;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.ItemMapper;
import com.project.clothingaggregator.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public EbayItemDto createItem(EbayItemDto itemRequest) {
        return itemMapper.toResponse(itemRepository
                .save(itemMapper.toEntity(itemRequest)));
    }

    public EbayItemDto getItem(String id) {
        return itemMapper.toResponse(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    public List<EbayItemDto> getAllItems() {
        return itemRepository.findAll().stream().map(itemMapper::toResponse).toList();
    }

    public EbayItemDto updateItem(String id, EbayItemDto request) {
        EbayClothingItem item = itemRepository.findById(id).orElseThrow(NotFoundException::new);

        return itemMapper.toResponse(itemRepository
                .save(itemMapper.updateFromRequest(request, item)));
    }

    public void deleteItem(String id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item with id = " + id + " not found");
        }
        itemRepository.deleteById(id);
    }

}
