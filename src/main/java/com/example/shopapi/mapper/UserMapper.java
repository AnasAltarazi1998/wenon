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

    private final ShopMapper shopMapper;
    public UserMapper(ModelMapper modelMapper, ShopMapper shopMapper) {
        this.modelMapper = modelMapper;
        this.shopMapper = shopMapper;
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
                .map(shopMapper::toDto)
                .collect(Collectors.toSet()));
        }

        return userDto;
    }

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
