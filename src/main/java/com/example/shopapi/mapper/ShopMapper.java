package com.example.shopapi.mapper;

import com.example.shopapi.model.BankDto;
import com.example.shopapi.model.ContactDto;
import com.example.shopapi.model.LocationDto;
import com.example.shopapi.model.Shop;
import com.example.shopapi.model.ShopDto;
import com.example.shopapi.model.UserDto;
import com.example.shopapi.model.UserRole;
import com.example.shopapi.repository.UserRepository;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public ShopMapper(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public ShopDto toDto(Shop shop) {
        // Create a new ShopDto and set fields manually to avoid ModelMapper issues
        ShopDto shopDto = new ShopDto();
        shopDto.setId(shop.getId());
        shopDto.setName(shop.getName());
        shopDto.setDescription(shop.getDescription());
        shopDto.setCity(shop.getCity());
        shopDto.setCategory(shop.getCategory());
        shopDto.setImageUrl(shop.getImageUrl());
        shopDto.setOpenTime(shop.getOpenTime());
        shopDto.setCloseTime(shop.getCloseTime());
        shopDto.setWorkStatus(shop.getWorkStatus());

        // Handle contact
        if (shop.getContact() != null) {
            shopDto.setContact(modelMapper.map(shop.getContact(), ContactDto.class));
        }

        // Handle location
        if (shop.getLocation() != null) {
            shopDto.setLocation(modelMapper.map(shop.getLocation(), LocationDto.class));
        }

        // Handle banks
        if (shop.getBanks() != null) {
            shopDto.setBanks(shop.getBanks().stream()
                .map(bank -> modelMapper.map(bank, BankDto.class))
                .collect(Collectors.toSet()));
        }

        // Set owner name instead of UserDto to prevent circular reference
        if (shop.getOwner() != null) {
            shopDto.setOwner(shop.getOwner().getUsername());
        }

        return shopDto;
    }

    public Shop toEntity(ShopDto shopDto) {
        Shop shop = modelMapper.map(shopDto, Shop.class);

        // Handle owner field - convert username to User entity
        if (shopDto.getOwner() != null && !shopDto.getOwner().isEmpty()) {
            userRepository.findByUsername(shopDto.getOwner())
                .ifPresent(shop::setOwner);
        }

        return shop;
    }
}
