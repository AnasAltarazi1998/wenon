package com.example.shopapi.mapper;

import com.example.shopapi.model.User;
import com.example.shopapi.model.UserDto;
import com.example.shopapi.model.ShopDto;
import com.example.shopapi.model.Shop;
import java.util.stream.Collectors;
import java.util.HashSet;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toDto(User user) {
        // Manually map User to UserDto to avoid circular reference issues
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        // Don't map password for security reasons
        userDto.setRole(user.getRole());
        userDto.setActive(user.isActive());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        // Handle shops to prevent circular reference
        if (user.getShops() != null) {
            userDto.setShops(user.getShops().stream()
                .map(shop -> {
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
                    shopDto.setOwner(user.getUsername()); // Set owner as username string
                    // Don't set banks, contact, or location to keep it simple
                    return shopDto;
                })
                .collect(Collectors.toSet()));
        }

        return userDto;
    }

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
