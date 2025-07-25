package com.example.shopapi.service;

import com.example.shopapi.model.ShopDto;
import com.example.shopapi.exception.ResourceNotFoundException;
import com.example.shopapi.model.Shop;
import com.example.shopapi.repository.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.shopapi.mapper.ShopMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final ShopMapper shopMapper;

    public ShopService(ShopRepository shopRepository, ModelMapper modelMapper,ShopMapper shopMapper) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
        this.shopMapper = shopMapper;
    }

    public List<ShopDto> getAllShops() {
        log.info("Fetching all shops");
        return shopRepository.findAll().stream()
                .map(shopMapper::toDto)
                .collect(Collectors.toList());
    }

    public ShopDto getShopById(Long id) {
        log.info("Fetching shop with id: {}", id);
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
        return convertToDto(shop);
    }

    public List<ShopDto> getShopsByCity(String city) {
        log.info("Fetching shops in city: {}", city);
        return shopRepository.findByCity(city).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ShopDto> getShopsByCategory(String category) {
        log.info("Fetching shops in category: {}", category);
        return shopRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ShopDto> getShopsByWorkStatus(String workStatus) {
        log.info("Fetching shops with work status: {}", workStatus);
        return shopRepository.findByWorkStatus(workStatus).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ShopDto convertToDto(Shop shop) {
        if (shop == null) {
            return null;
        }
        return shopMapper.toDto(shop);
    }

    public Shop convertToEntity(ShopDto shopDto) {
        if (shopDto == null) {
            return null;
        }
        return shopMapper.toEntity(shopDto);
    }
}
